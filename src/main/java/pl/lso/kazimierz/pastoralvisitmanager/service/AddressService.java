package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.BadRequestException;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.comparator.ApartmentComparator;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressData;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trim;

@Service
public class AddressService {

    private AddressRepository addressRepository;
    private ApartmentRepository apartmentRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, ApartmentRepository apartmentRepository) {
        this.addressRepository = addressRepository;
        this.apartmentRepository = apartmentRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Page<Address> getChunk(Pageable pageable, String name) {
        return addressRepository.findAllByName(pageable, name);
    }

    public Address getAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);
        if(!address.isPresent()) {
            throw new NotFoundException("Address not found");
        }
        address.get().getApartments().sort(new ApartmentComparator());
        return address.get();
    }

    @Transactional
    public Address addNewAddress(AddressData addressData) {
        if(addressData == null) {
            throw new NotFoundException("Address data not found");
        }

        String prefix = trim(addressData.getPrefix());
        if(isEmpty(prefix)) {
            prefix = null;
        }

        Address address = new Address();
        address.setPrefix(prefix);
        address.setStreetName(trim(addressData.getStreetName()));
        address.setBlockNumber(trim(addressData.getBlockNumber()));

        address = addressRepository.save(address);
        saveApartments(address, addressData.getApartments());
        return address;
    }

    @Transactional
    public Address updateAddress(Long addressId, AddressData addressData) {
        Optional<Address> address = addressRepository.findById(addressId);
        if(!address.isPresent()) {
            throw new NotFoundException("Address not found");
        }
        Address existingAddress = addressRepository.findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(addressData.getStreetName(), addressData.getBlockNumber());
        if(existingAddress != null && !existingAddress.getId().equals(addressId)) {
            throw new BadRequestException("Such an address already exists");
        }

        String prefix = trim(addressData.getPrefix());
        if(isEmpty(prefix)) {
            prefix = null;
        }

        address.get().setPrefix(prefix);
        address.get().setStreetName(trim(addressData.getStreetName()));
        address.get().setBlockNumber(trim(addressData.getBlockNumber()));

        Address savedAddress = addressRepository.save(address.get());
        saveApartments(savedAddress, addressData.getApartments());
        return savedAddress;
    }

    private void saveApartments(Address address, List<String> apartments) {
        if(isEmpty(apartments)) {
           throw new BadRequestException("Address should have at least one apartment");
        }
        List<Apartment> existingApartments = apartmentRepository.findByAddressId(address.getId());
        List<String> existingApartmentNumbers = existingApartments.stream()
                .map(Apartment::getNumber)
                .collect(Collectors.toList());

        List<Apartment> apartmentsToRemove = existingApartments.stream()
                .filter(apartment -> !apartments.contains(apartment.getNumber()))
                .collect(Collectors.toList());
        for(Apartment apartment : apartmentsToRemove) {
            apartmentRepository.delete(apartment);
        }

        apartments.removeAll(existingApartmentNumbers);

        for(String number : apartments) {
            saveApartment(address, number);
        }
    }

    private void saveApartment(Address address, String number) {
        Apartment apartment = new Apartment();
        apartment.setAddress(address);
        apartment.setNumber(number);
        apartmentRepository.save(apartment);
    }
}
