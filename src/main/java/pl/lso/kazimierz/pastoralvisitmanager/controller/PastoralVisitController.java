package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.AddressDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.ApartmentDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.PastoralVisitDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.PriestDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.NewPastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.service.ApartmentService;
import pl.lso.kazimierz.pastoralvisitmanager.service.PastoralVisitService;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pastoralvisit")
public class PastoralVisitController {

    private PastoralVisitService pastoralVisitService;

    @Autowired
    public PastoralVisitController(PastoralVisitService pastoralVisitService) {
        this.pastoralVisitService = pastoralVisitService;
    }

    @PostMapping("")
    public ResponseEntity addNewPastoralVisit(@RequestBody @Validated NewPastoralVisit newPastoralVisit) {
        PastoralVisit pastoralVisit = pastoralVisitService.addNewPastoralVisit(newPastoralVisit);

        Map<String, Object> response = new HashMap<>();
        response.put("pastoralVisit", PastoralVisitDtoBuilder.buildFromEntity(pastoralVisit));
        response.put("response", "New pastoral visit has been added");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePastoralVisit(@PathVariable("id") Long id, @RequestBody @Validated NewPastoralVisit newPastoralVisit) {
        PastoralVisit pastoralVisit = pastoralVisitService.updatePastoralVisit(id, newPastoralVisit);

        Map<String, Object> response = new HashMap<>();
        response.put("pastoralVisit", PastoralVisitDtoBuilder.buildFromEntity(pastoralVisit));
        response.put("response", "New pastoral visit has been added");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
