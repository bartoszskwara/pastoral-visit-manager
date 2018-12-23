package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;
import java.util.StringJoiner;

import static pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat.CSV;
import static pl.lso.kazimierz.pastoralvisitmanager.service.util.PastoralVisitUtils.getPastoralVisitStatus;

@Service
public class CsvExportService extends ZipExportService {

    private static final String DELIMITER = ";";
    private static final String END_LINE = System.lineSeparator();

    public byte[] export(List<SelectedAddress> selectedAddresses) {
        return createZipFile(selectedAddresses, CSV);
    }

    @Override
    public byte[] createFileContent(SelectedAddress selectedAddress) {
        List<Apartment> apartments = sortApartments(selectedAddress.getAddress().getApartments());

        StringJoiner lineJoiner = new StringJoiner(END_LINE);
        lineJoiner.add(createFileHeader(selectedAddress.getSeasons(), selectedAddress.getEmptyColumnsCount()));
        for(Apartment apartment : apartments) {
            StringJoiner joiner = new StringJoiner(DELIMITER);
            joiner.add(apartment.getNumber());
            for(Season season : selectedAddress.getSeasons()) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                joiner.add(status != null ? status.getStatus() : "");
            }
            for(int i = 0; i < selectedAddress.getEmptyColumnsCount(); i++) {
                joiner.add(DELIMITER);
            }
            lineJoiner.add(joiner.toString());
        }
        return lineJoiner.toString().getBytes();
    }

    private String createFileHeader(List<Season> seasons, Integer emptyColumnsCount) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add("Apartment");
        for(Season season : seasons) {
            joiner.add(season.getName());
        }
        for(int i = 0; i < emptyColumnsCount; i++) {
            joiner.add(DELIMITER);
        }
        return joiner.toString();
    }
}
