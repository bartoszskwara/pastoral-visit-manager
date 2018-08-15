package pl.lso.kazimierz.pastoralvisitmanager.service.importing;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImportRequestDtoList {

    private List<ImportRequestDto> requests;
    private String test;
}
