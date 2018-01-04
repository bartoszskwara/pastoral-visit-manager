package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.NewApartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;


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

    public Apartment addNewApartment(NewApartment newApartment) {
        if(newApartment == null) {
            throw new NotFoundException("Apartment data not found");
        }
        Address address = addressRepository.findOne(newApartment.getAddressId());
        if(address == null) {
            throw new NotFoundException("Address not found");
        }

        Apartment apartment = new Apartment();
        apartment.setNumber(newApartment.getNumber());
        apartment.setAddress(address);
        return apartmentRepository.save(apartment);
    }
}
