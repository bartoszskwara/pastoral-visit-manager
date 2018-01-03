package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;

import java.util.HashSet;
import java.util.Set;

public class PriestDtoBuilder {

    private Long id;
    private String name;
    private Set<PastoralVisitDto> pastoralVisits;

    public PriestDtoBuilder() {}

    public static PriestDtoBuilder getInstance() {
        return new PriestDtoBuilder();
    }

    public PriestDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PriestDtoBuilder withValue(String name) {
        this.name = name;
        return this;
    }

    public PriestDtoBuilder withPastoralVisit(PastoralVisitDto pastoralVisit) {
        if(this.pastoralVisits == null) {
            this.pastoralVisits = new HashSet<>();
        }
        this.pastoralVisits.add(pastoralVisit);
        return this;
    }

    public PriestDtoBuilder withPastoralVisits(Set<PastoralVisitDto> pastoralVisits) {
        this.pastoralVisits = pastoralVisits;
        return this;
    }

    public PriestDto build() {
        return new PriestDto(
                this.id,
                this.name,
                this.pastoralVisits
        );
    }
}
