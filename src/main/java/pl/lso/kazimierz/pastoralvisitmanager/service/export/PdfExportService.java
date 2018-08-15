package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.itextpdf.text.Font.FontFamily.TIMES_ROMAN;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat.PDF;
import static pl.lso.kazimierz.pastoralvisitmanager.service.util.PastoralVisitUtils.getPastoralVisitStatus;

@Service
public class PdfExportService extends ZipExportService {

    private static final Font DEFAULT_FONT = new Font(TIMES_ROMAN, 10);

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
            Paragraph title = createStreetName(selectedAddress.getAddress());
            PdfPTable table = createTable(selectedAddress);
            document.add(title);
            document.add(table);
            document.close();
            return out.toByteArray();
        }
        catch (DocumentException e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    private PdfPTable createTable(SelectedAddress selectedAddress) {
        PdfPTable table = new PdfPTable(getColumnsCount(selectedAddress));
        table.setTotalWidth(200);
        table.setLockedWidth(true);
        createHeader(table, selectedAddress.getSeasons(), selectedAddress.getEmptyColumnsCount());
        createRows(table, selectedAddress);
        return table;
    }

    private Integer getColumnsCount(SelectedAddress selectedAddress) {
        int count = isNotEmpty(selectedAddress.getSeasons()) ? selectedAddress.getSeasons().size() : 0;
        count += selectedAddress.getEmptyColumnsCount() != null ? selectedAddress.getEmptyColumnsCount() : 0;
        count += 2;
        return count;
    }

    private Paragraph createStreetName(Address address) {
        Paragraph title = new Paragraph(String.format("%s %s", address.getStreetName(), address.getBlockNumber()), DEFAULT_FONT);
        title.setAlignment(Element.ALIGN_LEFT);
        return title;
    }

    private void createHeader(PdfPTable table, List<Season> seasons, Integer emptyColumnsCount) {
        List<String> columns = new ArrayList<>();
        columns.add("");
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
        int count = 1;
        for(Apartment apartment : selectedAddress.getAddress().getApartments()) {
            table.addCell(cell(String.valueOf(count)));
            table.addCell(cell(apartment.getNumber()));

            for(Season season : selectedAddress.getSeasons()) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                table.addCell(cell(status != null ? status.getStatus() : ""));
            }
            for(int i = 0; i < selectedAddress.getEmptyColumnsCount(); i++) {
                table.addCell(emptyCell());
            }
            count++;
        }
    }

    private PdfPCell cell(String content) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(content, DEFAULT_FONT));
        return cell;
    }

    private PdfPCell emptyCell() {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase("", DEFAULT_FONT));
        return cell;
    }
}