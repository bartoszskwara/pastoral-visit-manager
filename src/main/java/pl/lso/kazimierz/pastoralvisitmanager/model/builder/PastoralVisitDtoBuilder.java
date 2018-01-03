package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;

import java.util.Date;

public class PastoralVisitDtoBuilder {

    private Long id;
    private String value;
    private Date date;
    private ApartmentDto apartment;
    private PriestDto priest;

    public PastoralVisitDtoBuilder() {}

    public static PastoralVisitDtoBuilder getInstance() {
        return new PastoralVisitDtoBuilder();
    }

    public PastoralVisitDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PastoralVisitDtoBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public PastoralVisitDtoBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public PastoralVisitDtoBuilder withApartment(ApartmentDto apartment) {
        this.apartment = apartment;
        return this;
    }

    public PastoralVisitDtoBuilder withPriest(PriestDto priest) {
        this.priest = priest;
        return this;
    }

    public PastoralVisitDto build() {
        return new PastoralVisitDto(
                this.id,
                this.value,
                this.date,
                this.apartment,
                this.priest
        );
    }
}
