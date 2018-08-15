package pl.lso.kazimierz.pastoralvisitmanager.service.importing;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportRequestDto {

    private String streetName;
    private String blockNumber;
    private Long priestId;
    private MultipartFile file;
}
