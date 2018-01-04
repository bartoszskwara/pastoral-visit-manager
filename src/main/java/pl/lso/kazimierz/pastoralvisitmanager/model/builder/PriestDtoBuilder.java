package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public PriestDtoBuilder withName(String name) {
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

    public static PriestDto buildFromEntity(Priest priest) {
        Set<PastoralVisitDto> pastoralVisits = priest.getPastoralVisits().stream()
                .map(p -> {
                    AddressDto address = AddressDtoBuilder.getInstance()
                            .withId(p.getApartment().getAddress().getId())
                            .withStreetName(p.getApartment().getAddress().getStreetName())
                            .withBlockNumber(p.getApartment().getAddress().getBlockNumber())
                            .build();
                    ApartmentDto apartment = ApartmentDtoBuilder.getInstance()
                            .withId(p.getApartment().getId())
                            .withNumber(p.getApartment().getNumber())
                            .withAddress(address)
                            .build();
                    return PastoralVisitDtoBuilder.getInstance()
                            .withId(p.getId())
                            .withDate(p.getDate())
                            .withValue(p.getValue())
                            .withApartment(apartment)
                            .build();
                })
                .collect(Collectors.toSet());

        return PriestDtoBuilder.getInstance()
                .withId(priest.getId())
                .withName(priest.getName())
                .withPastoralVisits(pastoralVisits)
                .build();
    }

    public PriestDto build() {
        return new PriestDto(
                this.id,
                this.name,
                this.pastoralVisits
        );
    }
}
