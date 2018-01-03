package pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import java.util.Set;

public class PriestDto {
    private Long id;
    private String name;
    private Set<PastoralVisitDto> pastoralVisits;

    public PriestDto(Long id, String name, Set<PastoralVisitDto> pastoralVisits) {
        this.id = id;
        this.name = name;
        this.pastoralVisits = pastoralVisits;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<PastoralVisitDto> getPastoralVisits() {
        return pastoralVisits;
    }
}
