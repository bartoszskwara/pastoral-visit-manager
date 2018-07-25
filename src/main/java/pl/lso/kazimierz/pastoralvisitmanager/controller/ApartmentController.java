package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.NewApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.ApartmentService;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    private ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping({"", "/"})
    public ResponseEntity addNewApartment(@RequestBody @Validated NewApartmentDto newApartmentDto) {
        apartmentService.addNewApartment(newApartmentDto);
        return ResponseEntity.ok().build();
    }
}
