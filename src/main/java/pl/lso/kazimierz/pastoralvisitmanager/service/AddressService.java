package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.AddressDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.ApartmentDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.PastoralVisitDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Page<Address> getAllAddressesOrByName(Pageable pageable, String streetName) {
        return addressRepository.findByStreetNameContainingIgnoreCase(pageable, streetName);
    }

    public AddressDto getAddressDetails(Long addressId) {
        Address address = addressRepository.findOne(addressId);
        if(address == null) {
            throw new NotFoundException("Address not found");
        }

        List<ApartmentDto> apartmentList = address.getApartments().stream()
                .map(apartment -> {
                    Set<PastoralVisitDto> pastoralVisitDtoSet = apartment.getPastoralVisits().stream()
                            .map(visit -> PastoralVisitDtoBuilder.getInstance()
                                    .withId(visit.getId())
                                    .withDate(visit.getDate())
                                    .withValue(visit.getValue())
                                    .withApartment(null)
                                    .withPriest(null)
                                    .build()
                            )
                            .collect(Collectors.toSet());

                    return ApartmentDtoBuilder.getInstance()
                            .withId(apartment.getId())
                            .withNumber(apartment.getNumber())
                            .withAddress(null)
                            .withApartmentHistories(null)
                            .withPastoralVisits(pastoralVisitDtoSet)
                            .build();
                })
                .sorted(Comparator.comparing(ApartmentDto::getNumber))
                .collect(Collectors.toList());

        return AddressDtoBuilder.getInstance()
                .withId(address.getId())
                .withStreetName(address.getStreetName())
                .withBlockNumber(address.getBlockNumber())
                .withApartments(apartmentList)
                .build();
    }

    @Transactional
    public Address addNewAddress(NewAddress newAddress) {
        if(newAddress == null) {
            throw new NotFoundException("Address data not found");
        }

        Address address = new Address();
        address.setStreetName(newAddress.getStreetName());
        address.setBlockNumber(newAddress.getBlockNumber());

        Set<Apartment> apartments = new HashSet<>();
        for(int i = newAddress.getApartmentsFrom(); i <= newAddress.getApartmentsTo(); i++) {
            if(!newAddress.getExcluded().contains(i)) {
                Apartment apartment = new Apartment();
                apartment.setNumber(String.valueOf(i));
                apartment.setAddress(address);
                apartments.add(apartment);
            }
        }

        for(String i : newAddress.getIncluded()) {
            Apartment apartment = new Apartment();
            apartment.setNumber(String.valueOf(i));
            apartment.setAddress(address);
            apartments.add(apartment);
        }

        address.setApartments(apartments);
        return addressRepository.save(address);
    }
}
