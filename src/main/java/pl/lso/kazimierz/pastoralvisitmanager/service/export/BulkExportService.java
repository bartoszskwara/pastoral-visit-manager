package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.exception.ServerException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.replaceAll;
import static org.apache.commons.lang3.StringUtils.trim;
import static pl.lso.kazimierz.pastoralvisitmanager.service.util.PastoralVisitUtils.getPastoralVisitStatus;

@Service
public class BulkExportService {

    private static final String DELIMITER = ";";
    private static final String SEASON_NAME_DELIMITER = "+";
    private static final String END_LINE = System.lineSeparator();

    private final AddressRepository addressRepository;

    private final SeasonRepository seasonRepository;

    @Autowired
    public BulkExportService(AddressRepository addressRepository, SeasonRepository seasonRepository) {
        this.addressRepository = addressRepository;
        this.seasonRepository = seasonRepository;
    }

    public byte[] exportBulk(List<SelectedAddressDto> selectedAddressDtos, String format) {
        if(ExportFileFormat.getByName(format) == null) {
            throw new NotFoundException("File format is not supported");
        }

        List<Long> addressIds = selectedAddressDtos.stream().map(SelectedAddressDto::getAddressId).collect(Collectors.toList());
        List<Address> addresses = addressRepository.findAllById(addressIds);
        if(isEmpty(addresses)) {
            throw new NotFoundException("Addresses not found");
        }

        List<Season> seasons = seasonRepository.findAll();
        if(isEmpty(seasons)) {
            throw new NotFoundException("No seasons");
        }

        List<SelectedAddress> selectedAddresses = mapSelectedAddresses(selectedAddressDtos, addresses, seasons);
        return createZipFile(selectedAddresses, format);
    }

    private List<SelectedAddress> mapSelectedAddresses(List<SelectedAddressDto> selectedAddressDtos, List<Address> addresses, List<Season> seasons) {
        List<SelectedAddress> result = new ArrayList<>();
        for (SelectedAddressDto selectedAddressDto : selectedAddressDtos) {
            Address address = findAddressById(selectedAddressDto.getAddressId(), addresses);
            if(address != null) {
                List<Season> seasonsForAddress = findSeasonsById(selectedAddressDto.getSeasons(), seasons);
                if(isNotEmpty(seasonsForAddress)) {
                    SelectedAddress selectedAddress = new SelectedAddress();
                    selectedAddress.setAddress(address);
                    selectedAddress.setSeasons(seasonsForAddress);
                    result.add(selectedAddress);
                }
            }
        }
        return result;
    }

    private Address findAddressById(Long addressId, List<Address> addresses) {
        Optional<Address> address = addresses.stream().filter(a -> a.getId().equals(addressId)).findFirst();
        return address.orElse(null);
    }

    private List<Season> findSeasonsById(List<Long> seasonIds, List<Season> seasons) {
        return seasons.stream().filter(s -> seasonIds.contains(s.getId())).collect(Collectors.toList());
    }

    private byte[] createZipFile(List<SelectedAddress> selectedAddresses, String entryFormat) {
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try(ZipOutputStream zip = new ZipOutputStream(content)) {
            for(SelectedAddress selectedAddress : selectedAddresses) {
                String entryName = format("%s%s_%s.%s",
                        selectedAddress.getAddress().getStreetName(),
                        selectedAddress.getAddress().getBlockNumber(),
                        joinSeasonNames(selectedAddress.getSeasons()),
                        trim(entryFormat));
                String fileContent = createFileContent(selectedAddress);
                putZipEntry(zip, fileContent, trim(replaceAll(entryName, "\\s", "")));
            }
        } catch(IOException e) {
            throw new ServerException("Creating zip file failed");
        }
        return content.toByteArray();
    }

    private String createFileContent(SelectedAddress selectedAddress) {
        StringJoiner lineJoiner = new StringJoiner(END_LINE);
        lineJoiner.add(createFileHeader(selectedAddress.getSeasons()));
        int count = 1;
        for(Apartment apartment : selectedAddress.getAddress().getApartments()) {
            StringJoiner joiner = new StringJoiner(DELIMITER);
            joiner.add(String.valueOf(count))
                    .add(apartment.getNumber());
            for(Season season : selectedAddress.getSeasons()) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                joiner.add(status != null ? status.getStatus() : "");
            }
            lineJoiner.add(joiner.toString());
            count++;
        }
        return lineJoiner.toString();
    }

    private String createFileHeader(List<Season> seasons) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add("").add("Apartment");
        for(Season season : seasons) {
            joiner.add(season.getName());
        }
        return joiner.toString();
    }

    private void putZipEntry(ZipOutputStream zipFile, String entryContent, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        entry.setSize(entryContent.getBytes().length);
        zipFile.putNextEntry(entry);
        zipFile.write(entryContent.getBytes());
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
