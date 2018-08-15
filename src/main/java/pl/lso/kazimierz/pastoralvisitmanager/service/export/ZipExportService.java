package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import pl.lso.kazimierz.pastoralvisitmanager.exception.ServerException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.replaceAll;
import static org.apache.commons.lang3.StringUtils.trim;

abstract class ZipExportService implements FileContentProvider {

    private static final String SEASON_NAME_DELIMITER = "+";

    byte[] createZipFile(List<SelectedAddress> selectedAddresses, ExportFileFormat fileFormat) {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try(ZipOutputStream zip = new ZipOutputStream(content)) {
            for(SelectedAddress selectedAddress : selectedAddresses) {
                String entryName = format("%s%s_%s.%s",
                        selectedAddress.getAddress().getStreetName(),
                        selectedAddress.getAddress().getBlockNumber(),
                        joinSeasonNames(selectedAddress.getSeasons()),
                        fileFormat.name());
                byte[] fileContent = createFileContent(selectedAddress);
                putZipEntry(zip, fileContent, trim(replaceAll(entryName, "\\s", "")));
            }
        } catch(IOException e) {
            throw new ServerException("Creating zip file failed");
        }
        return content.toByteArray();
    }

    private void putZipEntry(ZipOutputStream zipFile, byte[] entryContent, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        entry.setSize(entryContent.length);
        zipFile.putNextEntry(entry);
        zipFile.write(entryContent);
        zipFile.closeEntry();
    }

    private String joinSeasonNames(List<Season> seasons) {
        StringJoiner joiner = new StringJoiner(SEASON_NAME_DELIMITER);
        for (Season season : seasons) {
            joiner.add(season.getName());
        }
        return joiner.toString();
    }
}
