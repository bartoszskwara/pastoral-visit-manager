package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory.ApartmentHistoryDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static ApartmentDto buildFromEntity(Apartment apartment) {
        AddressDto addressDto = AddressDtoBuilder.getInstance()
                .withId(apartment.getAddress().getId())
                .withStreetName(apartment.getAddress().getStreetName())
                .withBlockNumber(apartment.getAddress().getBlockNumber())
                .withApartments(null)
                .build();

        Set<ApartmentHistoryDto> apartmentHistories = apartment.getApartmentHistories().stream()
                .map(a -> ApartmentHistoryDtoBuilder.getInstance()
                        .withId(a.getId())
                        .withDate(a.getDate())
                        .withComment(a.getComment())
                        .build())
                .collect(Collectors.toSet());

        Set<PastoralVisitDto> pastoralVisits = apartment.getPastoralVisits().stream()
                .map(p -> {
                    PriestDto priest = PriestDtoBuilder.getInstance()
                            .withId(p.getPriest().getId())
                            .withName(p.getPriest().getName())
                            .build();
                    return PastoralVisitDtoBuilder.getInstance()
                            .withId(p.getId())
                            .withValue(p.getValue())
                            .withDate(p.getDate())
                            .withPriest(priest)
                            .build();
                })
                .collect(Collectors.toSet());

        return ApartmentDtoBuilder.getInstance()
                .withId(apartment.getId())
                .withNumber(apartment.getNumber())
                .withAddress(addressDto)
                .withApartmentHistories(apartmentHistories)
                .withPastoralVisits(pastoralVisits)
                .build();
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
