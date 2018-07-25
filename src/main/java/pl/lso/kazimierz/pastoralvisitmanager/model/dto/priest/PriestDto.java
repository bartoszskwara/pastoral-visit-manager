package pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PriestDto {
    private Long id;
    private String name;
    private List<Long> pastoralVisits;
}
