package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import java.util.Set;

public class AddressDto {
    private Long id;
    private String streetName;
    private Integer blockNumber;
    private Set<ApartmentDto> apartments;

    public AddressDto(Long id, String streetName, Integer blockNumber, Set<ApartmentDto> apartments) {
        this.id = id;
        this.streetName = streetName;
        this.blockNumber = blockNumber;
        this.apartments = apartments;
    }

    public Long getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public Set<ApartmentDto> getApartments() {
        return apartments;
    }
}
