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
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.NewApartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
