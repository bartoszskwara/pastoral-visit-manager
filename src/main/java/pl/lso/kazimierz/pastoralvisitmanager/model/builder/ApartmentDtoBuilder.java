package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory.ApartmentHistoryDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;

import java.util.HashSet;
import java.util.Set;

public class ApartmentDtoBuilder {

    private Long id;
    private String number;
    private AddressDto address;
    private Set<ApartmentHistoryDto> apartmentHistories;
    private Set<PastoralVisitDto> pastoralVisits;

    public ApartmentDtoBuilder() {
        apartmentHistories = new HashSet<>();
        pastoralVisits = new HashSet<>();
    }

    public static ApartmentDtoBuilder getInstance() {
        return new ApartmentDtoBuilder();
    }

    public ApartmentDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ApartmentDtoBuilder withNumber(String number) {
        this.number = number;
        return this;
    }

    public ApartmentDtoBuilder withAddress(AddressDto address) {
        this.address = address;
        return this;
    }

    public ApartmentDtoBuilder withApartmentHistory(ApartmentHistoryDto apartmentHistory) {
        if(this.apartmentHistories == null) {
            this.apartmentHistories = new HashSet<>();
        }
        this.apartmentHistories.add(apartmentHistory);
        return this;
    }

    public ApartmentDtoBuilder withApartmentHistories(Set<ApartmentHistoryDto> apartmentHistories) {
        this.apartmentHistories = apartmentHistories;
        return this;
    }

    public ApartmentDtoBuilder withPastoralVisit(PastoralVisitDto pastoralVisit) {
        if(this.pastoralVisits == null) {
            this.pastoralVisits = new HashSet<>();
        }
        this.pastoralVisits.add(pastoralVisit);
        return this;
    }

    public ApartmentDtoBuilder withPastoralVisits(Set<PastoralVisitDto> pastoralVisits) {
        this.pastoralVisits = pastoralVisits;
        return this;
    }

    public ApartmentDto build() {
        return new ApartmentDto(
                this.id,
                this.number,
                this.address,
                this.apartmentHistories,
                this.pastoralVisits
        );
    }
}
