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
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        createHeader(table, selectedAddress.getSeasons(), selectedAddress.getEmptyColumnsCount());
        createRows(table, selectedAddress);
        return table;
    }

    private Integer getColumnsCount(SelectedAddress selectedAddress) {
        int count = isNotEmpty(selectedAddress.getSeasons()) ? selectedAddress.getSeasons().size() : 0;
        count += selectedAddress.getEmptyColumnsCount() != null ? selectedAddress.getEmptyColumnsCount() : 0;
        count += 1;
        return count;
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
        List<Season> seasons = seasonRepository.findAll(new Sort(DESC, "name"));
        LinkedHashMap<String, Long> summary = new LinkedHashMap<>();
        seasons.forEach(s -> {
            long count = pastoralVisitRepository.countPastoralVisitByAddressAndSeasonAndValue(address.getId(), s.getId(), asList(completed.getStatus(), individually.getStatus()));
            summary.put(s.getName(), count);
        });
        return summary;
    }

    private void createHeader(PdfPTable table, List<Season> seasons, Integer emptyColumnsCount) {
        List<String> columns = new ArrayList<>();
        columns.add("No.");
        for (Season season : seasons) {
            columns.add(season.getName());
        }
        for(int i = 0; i < emptyColumnsCount; i++) {
            columns.add("");
        }

        columns.forEach(columnTitle -> {
            table.addCell(cell(columnTitle));
        });
    }

    private void createRows(PdfPTable table, SelectedAddress selectedAddress) {
        List<Apartment> apartments = sortApartments(selectedAddress.getAddress().getApartments());

        for(Apartment apartment : apartments) {
            table.addCell(cell(apartment.getNumber()));

            for(Season season : selectedAddress.getSeasons()) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                table.addCell(cell(status != null ? status.getStatus() : ""));
            }
            for(int i = 0; i < selectedAddress.getEmptyColumnsCount(); i++) {
                table.addCell(emptyCell(1));
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