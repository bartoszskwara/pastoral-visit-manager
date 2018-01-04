package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory.ApartmentHistoryDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.ApartmentHistory;

import java.util.Date;

public class ApartmentHistoryDtoBuilder {

    private Long id;
    private String comment;
    private Date date;
    private ApartmentDto apartment;

    public ApartmentHistoryDtoBuilder() {}

    public static ApartmentHistoryDtoBuilder getInstance() {
        return new ApartmentHistoryDtoBuilder();
    }

    public ApartmentHistoryDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ApartmentHistoryDtoBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public ApartmentHistoryDtoBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public ApartmentHistoryDtoBuilder withApartment(ApartmentDto apartment) {
        this.apartment = apartment;
        return this;
    }

    public static ApartmentHistoryDto buildFromEntity(ApartmentHistory apartmentHistory) {
        ApartmentDto apartment = ApartmentDtoBuilder.getInstance()
                .withId(apartmentHistory.getApartment().getId())
                .withPastoralVisits(null)
                .withApartmentHistories(null)
                .build();
        return ApartmentHistoryDtoBuilder.getInstance()
                .withId(apartmentHistory.getId())
                .withDate(apartmentHistory.getDate())
                .withComment(apartmentHistory.getComment())
                .withApartment(apartment)
                .build();
    }

    public ApartmentHistoryDto build() {
        return new ApartmentHistoryDto(
                this.id,
                this.comment,
                this.date,
                this.apartment
        );
    }
}
