package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.common.CountResponseDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.PastoralVisitMapper;
import pl.lso.kazimierz.pastoralvisitmanager.service.PastoralVisitService;

@RestController
@RequestMapping("/pastoral-visit")
public class PastoralVisitController {

    private PastoralVisitService pastoralVisitService;

    @Autowired
    public PastoralVisitController(PastoralVisitService pastoralVisitService) {
        this.pastoralVisitService = pastoralVisitService;
    }

    @PostMapping("")
    public ResponseEntity savePastoralVisit(@RequestBody @Validated PastoralVisitDto pastoralVisitDto) {
        PastoralVisit pastoralVisit = pastoralVisitService.savePastoralVisit(pastoralVisitDto);
        return ResponseEntity.ok(PastoralVisitMapper.map(pastoralVisit));
    }

    @GetMapping("/address/{addressId}/season/last/completed")
    public ResponseEntity countCompletedPastoralVisitsInLastSeason(@PathVariable("addressId") Long addressId) {
        Long count = pastoralVisitService.countCompletedPastoralVisitsInLastSeason(addressId);
        return ResponseEntity.ok(new CountResponseDto(count));
    }
}
