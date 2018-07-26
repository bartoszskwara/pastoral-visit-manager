package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;


@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Page<Address> getAllAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Address getAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);
        if(!address.isPresent()) {
            throw new NotFoundException("Address not found");
        }
        return address.get();
    }

    @Transactional
    public void addNewAddress(NewAddressDto newAddressDto) {
        if(newAddressDto == null) {
            throw new NotFoundException("Address data not found");
        }

        Address address = new Address();
        address.setStreetName(newAddressDto.getStreetName());
        address.setBlockNumber(newAddressDto.getBlockNumber());

        List<Apartment> apartments = createApartments(address, newAddressDto.getApartmentsFrom(), newAddressDto.getApartmentsTo(),
                newAddressDto.getIncluded(), newAddressDto.getExcluded());

        address.setApartments(apartments);
        addressRepository.save(address);
    }

    private List<Apartment> createApartments(Address address, Integer from, Integer to, List<String> included, List<Integer> excluded) {
        included = included != null ? included : emptyList();
        excluded = excluded != null ? excluded : emptyList();

        List<Apartment> apartments = new ArrayList<>();
        for(int i = from; i <= to; i++) {
            if(!excluded.contains(i)) {
                Apartment apartment = new Apartment();
                apartment.setNumber(String.valueOf(i));
                apartment.setAddress(address);
                apartments.add(apartment);
            }
        }

        for(String i : included) {
            Apartment apartment = new Apartment();
            apartment.setNumber(String.valueOf(i));
            apartment.setAddress(address);
            apartments.add(apartment);
        }

        return apartments;
    }
}
