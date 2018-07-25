package pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApartmentDto {
    private Long id;
    private String number;
    private List<PastoralVisitDto> pastoralVisits;
}

