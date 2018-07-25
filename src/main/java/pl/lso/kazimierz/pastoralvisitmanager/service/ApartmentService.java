package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.NewApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;

import java.util.Optional;


@Service
public class ApartmentService {

    private ApartmentRepository apartmentRepository;
    private AddressRepository addressRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository,
                            AddressRepository addressRepository) {
        this.apartmentRepository = apartmentRepository;
        this.addressRepository = addressRepository;
    }

    public void addNewApartment(NewApartmentDto newApartmentDto) {
        if(newApartmentDto == null) {
            throw new NotFoundException("Apartment data not found");
        }
        Optional<Address> address = addressRepository.findById(newApartmentDto.getAddressId());
        if(!address.isPresent()) {
            throw new NotFoundException("Address not found");
        }

        Apartment apartment = new Apartment();
        apartment.setNumber(newApartmentDto.getNumber());
        apartment.setAddress(address.get());
        apartmentRepository.save(apartment);
    }
}
