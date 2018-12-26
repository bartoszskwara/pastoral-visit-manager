package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.common.EmptyColumn;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PastoralVisitRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.Font.FontFamily.TIMES_ROMAN;
import static com.itextpdf.text.Rectangle.NO_BORDER;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.util.StringUtils.isEmpty;
import static pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus.completed;
import static pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus.individually;
import static pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat.PDF;
import static pl.lso.kazimierz.pastoralvisitmanager.service.util.PastoralVisitUtils.getPastoralVisitStatus;

@Service
public class PdfExportService extends ZipExportService {

    private static final Font.FontFamily DEFAULT_FONT = TIMES_ROMAN;
    private static final int DEFAULT_FONT_SIZE = 10;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private PastoralVisitRepository pastoralVisitRepository;

    public byte[] export(List<SelectedAddress> selectedAddresses) {
        return createZipFile(selectedAddresses, PDF);
    }

    @Override
    public byte[] createFileContent(SelectedAddress selectedAddress) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();
            String title = createStreetName(selectedAddress.getAddress());
            PdfPTable summary = createSummary(title, selectedAddress.getAddress());
            PdfPTable addressTable = createTableForAddress(selectedAddress);
            PdfPTable layout = createTableLayout(summary, addressTable);
            document.add(layout);
            document.close();
            return out.toByteArray();
        }
        catch (DocumentException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    private PdfPTable createTableLayout(PdfPTable summary, PdfPTable addressTable) {
        PdfPTable layout = new PdfPTable(2);
        layout.setSplitLate(false);

        PdfPCell summaryCell = cell(summary, NO_BORDER);
        PdfPCell addressTableCell = cell(addressTable, NO_BORDER);

        try {
            layout.setWidths(new float[]{4, 5});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        layout.addCell(summaryCell);
        layout.addCell(addressTableCell);
        return layout;
    }

    private PdfPTable createTableForAddress(SelectedAddress selectedAddress) {
        PdfPTable table = new PdfPTable(getColumnsCount(selectedAddress));
        try {
            table.setWidths(calculateRelativeColumnWidths(selectedAddress));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        createHeader(table, selectedAddress.getSeasons(), selectedAddress.getEmptyColumns());
        createRows(table, selectedAddress);
        return table;
    }

    private Integer getColumnsCount(SelectedAddress selectedAddress) {
        int count = isNotEmpty(selectedAddress.getSeasons()) ? selectedAddress.getSeasons().size() : 0;
        count += isNotEmpty(selectedAddress.getEmptyColumns()) ? selectedAddress.getEmptyColumns().size() : 0;
        count += 1;
        return count;
    }

    private float[] calculateRelativeColumnWidths(SelectedAddress selectedAddress) {
        if(selectedAddress == null) {
            return new float[]{};
        }
        List<Float> widths = new ArrayList<>();
        widths.add(4f);
        selectedAddress.getSeasons().forEach(s -> widths.add(s.getName().length() + 0f));
        selectedAddress.getEmptyColumns().forEach(c -> {
            float w = isEmpty(c.getName()) ? 3f : c.getName().length() + 0f;
            if(w > 6f) {
                w = 6f;
            }
            if(w < 3f) {
                w = 3f;
            }
            widths.add(w);
        });
        float[] result = new float[widths.size()];
        for(int i = 0; i < widths.size(); i++) {
            result[i] = widths.get(i);
        }
        return result;
    }

    private String createStreetName(Address address) {
        return format("%s %s %s", address.getPrefix(), address.getStreetName(), address.getBlockNumber());
    }

    private PdfPTable createSummary(String title, Address address) {
        LinkedHashMap<String, Long> summaryContent = createSummaryContent(address);
        PdfPTable table = new PdfPTable(2);
        PdfPCell titleCell = cell(title, NO_BORDER, Font.BOLD, ALIGN_LEFT);
        titleCell.setColspan(2);

        table.addCell(titleCell);
        summaryContent.forEach((k, v) -> {
            PdfPCell cellSeason = cell(format("%s:", k), NO_BORDER, Font.NORMAL, ALIGN_LEFT);
            PdfPCell cellCompleted = cell(format("+%d", v), NO_BORDER, Font.NORMAL, ALIGN_LEFT);
            table.addCell(cellSeason);
            table.addCell(cellCompleted);
        });
        return table;
    }

    private LinkedHashMap<String, Long> createSummaryContent(Address address) {
        List<Season> seasons = seasonRepository.findNotCurrentOrderedByEndDate();
        LinkedHashMap<String, Long> summary = new LinkedHashMap<>();
        seasons.forEach(s -> {
            long count = pastoralVisitRepository.countPastoralVisitByAddressAndSeasonAndValue(address.getId(), s.getId(), asList(completed.getStatus(), individually.getStatus()));
            summary.put(s.getName(), count);
        });
        return summary;
    }

    private void createHeader(PdfPTable table, List<Season> seasons, List<EmptyColumn> emptyColumns) {
        table.addCell(cell("No."));
        if(isNotEmpty(seasons)) {
            seasons.forEach(s -> table.addCell(cell((s.getName()))));
        }
        if(isNotEmpty(emptyColumns)) {
            emptyColumns.forEach(c -> table.addCell(cell((c.getName()))));
        }
    }

    private void createRows(PdfPTable table, SelectedAddress selectedAddress) {
        List<Apartment> apartments = sortApartments(selectedAddress.getAddress().getApartments());

        for(Apartment apartment : apartments) {
            table.addCell(cell(apartment.getNumber()));

            for(Season season : selectedAddress.getSeasons()) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                table.addCell(cell(status != null ? status.getStatus() : ""));
            }

            if(isNotEmpty(selectedAddress.getEmptyColumns())) {
                selectedAddress.getEmptyColumns().forEach(c -> {
                    table.addCell(emptyCell(PdfPCell.BOX));
                });
            }
        }
    }

    private PdfPCell cell(String content) {
        return cell(content, PdfPCell.BOX);
    }

    private PdfPCell cell(PdfPTable content, int border) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(content);
        cell.setHorizontalAlignment(ALIGN_LEFT);
        cell.setBorder(border);
        return cell;
    }

    private PdfPCell cell(String content, int border) {
        return cell(content, border, Font.NORMAL, ALIGN_CENTER);
    }

    private PdfPCell cell(String content, int border, int style, int horizontalAlignment) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(border);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setPhrase(new Phrase(content, new Font(DEFAULT_FONT, DEFAULT_FONT_SIZE, style)));
        return cell;
    }

    private PdfPCell emptyCell(int border) {
        return cell("", border);
    }

}