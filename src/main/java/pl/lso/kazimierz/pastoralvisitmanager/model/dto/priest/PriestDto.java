package pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriestDto {
    private Long id;
    private String name;
}
