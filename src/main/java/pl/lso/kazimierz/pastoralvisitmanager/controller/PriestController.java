package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.PriestMapper;
import pl.lso.kazimierz.pastoralvisitmanager.service.PriestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/priest")
public class PriestController {

    private PriestService priestService;

    @Autowired
    public PriestController(PriestService priestService) {
        this.priestService = priestService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity getPriests() {
        List<PriestDto> priestDtos = priestService.getPriests().stream().map(PriestMapper::map).collect(Collectors.toList());
        return ResponseEntity.ok(priestDtos);
    }
}
