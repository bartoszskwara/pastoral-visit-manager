package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.service.export.BulkExportService;
import pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportFileFormat;
import pl.lso.kazimierz.pastoralvisitmanager.service.export.ExportService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportController {

    private ExportService exportService;

    private BulkExportService bulkExportService;

    @Autowired
    public ExportController(ExportService exportService, BulkExportService bulkExportService) {
        this.exportService = exportService;
        this.bulkExportService = bulkExportService;
    }

    @PostMapping("/address/bulk/format/{fileFormat}")
    public ResponseEntity exportBulkTo(@RequestBody List<SelectedAddressDto> selectedAddressDtos, @PathVariable("fileFormat") String fileFormat) {
        byte[] file = bulkExportService.exportBulk(selectedAddressDtos, fileFormat);
        String fileName = "export" + new Date() + "." + ExportFileFormat.getByName(fileFormat);

        return ResponseEntity.ok()
                .headers(prepareHeaders(fileName))
                .contentLength(file.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(file);
    }

    @GetMapping("/address/{addressId}/format/{fileFormat}")
    public ResponseEntity exportTo(@PathVariable("addressId") Long addressId, @PathVariable("fileFormat") String fileFormat) {

        byte[] file = exportService.export(addressId, fileFormat);
        String fileName = "export" + new Date() + "." + ExportFileFormat.getByName(fileFormat);

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
