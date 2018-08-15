package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportRequestDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportResponseDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportService;

import java.util.List;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;

    @Autowired
    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/address")
    public ResponseEntity importAddresses(@ModelAttribute ImportRequestDto importRequestDto) {
        List<ImportResponseDto> response = importService.importAddresses(importRequestDto);
        return ResponseEntity.ok(response);
    }
}
