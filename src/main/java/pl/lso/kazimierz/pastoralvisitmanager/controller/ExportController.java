package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat;
import pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportService;

import java.util.Date;

@RestController
@RequestMapping("/export")
public class ExportController {

    private ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/address/{addressId}/format/{fileFormat}")
    public ResponseEntity exportTo(@PathVariable("addressId") Long addressId, @PathVariable("fileFormat") String fileFormat) {

        byte[] file = exportService.export(addressId, fileFormat);
        String fileName = "export" + new Date() + "." + ExportFileFormat.getByName(fileFormat);

        HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(file);
    }
}
