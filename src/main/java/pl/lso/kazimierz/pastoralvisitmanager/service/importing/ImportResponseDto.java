package pl.lso.kazimierz.pastoralvisitmanager.service.importing;

import lombok.Data;

@Data
public class ImportResponseDto {

    private boolean completed;
    private String fileName;
    private String errorMessage;

}
