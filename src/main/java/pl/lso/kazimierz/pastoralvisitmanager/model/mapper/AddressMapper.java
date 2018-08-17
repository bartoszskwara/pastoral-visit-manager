package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SimpleAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

public class AddressMapper {

    public static SimpleAddressDto mapSimple(Address address) {
        return SimpleAddressDto.builder()
                .id(address.getId())
                .prefix(address.getPrefix())
                .streetName(address.getStreetName())
                .blockNumber(address.getBlockNumber())
                .apartmentCount(address.getApartments().size())
                .build();
    }

    public static AddressDto map(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .prefix(address.getPrefix())
                .streetName(address.getStreetName())
                .blockNumber(address.getBlockNumber())
                .apartments(mapApartments(address.getApartments()))
                .build();
    }

    private static List<ApartmentDto> mapApartments(List<Apartment> apartments) {
        if(isEmpty(apartments)) {
            return emptyList();
        }
        return apartments.stream().map(AddressMapper::mapApartmentDto).collect(Collectors.toList());
    }

    private static ApartmentDto mapApartmentDto(Apartment apartment) {
        return ApartmentDto.builder()
                .id(apartment.getId())
                .number(apartment.getNumber())
                .pastoralVisits(mapPastoralVisits(apartment.getPastoralVisits()))
                .build();
    }

    private static List<PastoralVisitDto> mapPastoralVisits(List<PastoralVisit> pastoralVisits) {
        if(isEmpty(pastoralVisits)) {
            return emptyList();
        }
        return pastoralVisits.stream().map(PastoralVisitMapper::map).collect(Collectors.toList());
    }
}
