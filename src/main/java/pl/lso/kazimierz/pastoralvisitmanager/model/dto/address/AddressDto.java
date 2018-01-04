package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private Long id;
    private String streetName;
    private Integer blockNumber;
    private List<ApartmentDto> apartments;

    public AddressDto(Long id, String streetName, Integer blockNumber, List<ApartmentDto> apartments) {
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

    public List<ApartmentDto> getApartments() {
        return apartments;
    }
}
