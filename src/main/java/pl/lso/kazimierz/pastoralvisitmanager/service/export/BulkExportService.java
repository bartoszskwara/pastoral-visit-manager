package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.common.EmptyColumn;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
public class BulkExportService {

    private final CsvExportService csvExportService;

    private final PdfExportService pdfExportService;

    private final AddressRepository addressRepository;

    private final SeasonRepository seasonRepository;

    @Autowired
    public BulkExportService(CsvExportService csvExportService, PdfExportService pdfExportService, AddressRepository addressRepository, SeasonRepository seasonRepository) {
        this.csvExportService = csvExportService;
        this.pdfExportService = pdfExportService;
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

        if(format != null && equalsIgnoreCase(format, "csv")) {
            return csvExportService.export(selectedAddresses);
        }

        if(format != null && equalsIgnoreCase(format, "pdf")) {
            return pdfExportService.export(selectedAddresses);
        }

        return new byte[]{};
    }

    private List<SelectedAddress> mapSelectedAddresses(List<SelectedAddressDto> selectedAddressDtos, List<Address> addresses, List<Season> seasons) {
        List<SelectedAddress> result = new ArrayList<>();
        for (SelectedAddressDto selectedAddressDto : selectedAddressDtos) {
            Address address = findAddressById(selectedAddressDto.getAddressId(), addresses);
            if(address != null) {
                List<Season> seasonsForAddress = findSeasonsById(selectedAddressDto.getSeasons(), seasons);
                if(isNotEmpty(seasonsForAddress) || isNotEmpty(selectedAddressDto.getEmptyColumns())) {
                    SelectedAddress selectedAddress = new SelectedAddress();
                    selectedAddress.setAddress(address);
                    selectedAddress.setSeasons(seasonsForAddress);
                    selectedAddress.setEmptyColumns(mapEmptyColumns(selectedAddressDto));
                    result.add(selectedAddress);
                }
            }
        }
        return result;
    }

    private List<Season> findSeasonsById(List<Long> seasonIds, List<Season> seasons) {
        return seasons.stream().filter(s -> seasonIds.contains(s.getId())).collect(Collectors.toList());
    }

    private List<EmptyColumn> mapEmptyColumns(SelectedAddressDto selectedAddressDto) {
        if(selectedAddressDto == null || isEmpty(selectedAddressDto.getEmptyColumns())) {
            return emptyList();
        }

        return selectedAddressDto.getEmptyColumns().stream()
                .map(c -> EmptyColumn.builder().id(c.getId()).name(c.getName()).build())
                .collect(Collectors.toList());
    }

    private Address findAddressById(Long addressId, List<Address> addresses) {
        Optional<Address> address = addresses.stream().filter(a -> a.getId().equals(addressId)).findFirst();
        return address.orElse(null);
    }
}
