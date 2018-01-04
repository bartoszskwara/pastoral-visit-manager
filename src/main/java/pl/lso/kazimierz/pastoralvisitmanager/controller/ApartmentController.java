package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.AddressDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.ApartmentDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.NewApartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.service.AddressService;
import pl.lso.kazimierz.pastoralvisitmanager.service.ApartmentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    private ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity addNewApartment(@RequestBody @Validated NewApartment newApartment) {
        Apartment apartment = apartmentService.addNewApartment(newApartment);

        Map<String, Object> response = new HashMap<>();
        response.put("apartment", ApartmentDtoBuilder.buildFromEntity(apartment));
        response.put("response", "New apartment has been added");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
