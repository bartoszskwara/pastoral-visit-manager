package pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory.ApartmentHistoryDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import java.util.Set;

public class ApartmentDto {
    private Long id;
    private Integer number;
    private AddressDto address;
    private Set<ApartmentHistoryDto> apartmentHistories;
    private Set<PastoralVisitDto> pastoralVisits;

    public ApartmentDto(Long id, Integer number, AddressDto address, Set<ApartmentHistoryDto> apartmentHistories, Set<PastoralVisitDto> pastoralVisits) {
        this.id = id;
        this.number = number;
        this.address = address;
        this.apartmentHistories = apartmentHistories;
        this.pastoralVisits = pastoralVisits;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public AddressDto getAddress() {
        return address;
    }

    public Set<ApartmentHistoryDto> getApartmentHistories() {
        return apartmentHistories;
    }

    public Set<PastoralVisitDto> getPastoralVisits() {
        return pastoralVisits;
    }
}
