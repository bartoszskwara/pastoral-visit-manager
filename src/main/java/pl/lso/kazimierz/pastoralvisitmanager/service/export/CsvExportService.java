package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.common.EmptyColumn;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;
import java.util.StringJoiner;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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
        lineJoiner.add(createFileHeader(selectedAddress.getSeasons(), selectedAddress.getEmptyColumns()));
        for(Apartment apartment : apartments) {
            StringJoiner joiner = new StringJoiner(DELIMITER);
            joiner.add(apartment.getNumber());
            if(isNotEmpty(selectedAddress.getSeasons())) {
                selectedAddress.getSeasons().forEach(season -> {
                    PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                    joiner.add(status != null ? status.getStatus() : "");
                });
            }
            if(isNotEmpty(selectedAddress.getEmptyColumns())) {
                selectedAddress.getEmptyColumns().forEach(c -> joiner.add(DELIMITER));
            }
            lineJoiner.add(joiner.toString());
        }
        return lineJoiner.toString().getBytes();
    }

    private String createFileHeader(List<Season> seasons, List<EmptyColumn> emptyColumns) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add("Apartment");
        if(isNotEmpty(seasons)) {
            seasons.forEach(season -> joiner.add(season.getName()));
        }
        if(isNotEmpty(emptyColumns)) {
            emptyColumns.forEach(c -> joiner.add(DELIMITER));
        }
        return joiner.toString();
    }
}
