package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportRequestDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportResponseDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.importing.ImportService;

import java.util.Date;
import java.util.List;

import static pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat.CSV;

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

    @GetMapping("/template")
    public ResponseEntity downloadTemplate() {
        byte[] file = importService.downloadTemplate();
        String fileName = "export" + (new Date()).toString() + "." + CSV;

        return ResponseEntity.ok()
                .headers(prepareHeaders(fileName))
                .contentLength(file.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(file);
    }

    private HttpHeaders prepareHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return headers;
    }
}
