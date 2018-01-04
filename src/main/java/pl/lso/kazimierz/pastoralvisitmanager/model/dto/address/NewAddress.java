package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.AddressUnique;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.ApartmentNumbersRange;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.HasAtLeastOneApartment;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@HasAtLeastOneApartment
@ApartmentNumbersRange
@AddressUnique
public class NewAddress {

    @NotNull(message = "Street name cannot be null")
    @Size(min=1, message = "Street name cannot be blank")
    private String streetName;

    @NotNull(message = "Block number cannot be null")
    @Size(min=1, message = "Block number cannot be blank")
    private String blockNumber;

    @Min(value = 1, message = "Apartment numbers cannot be lower than 1")
    private Integer apartmentsFrom;

    @Min(value = 1, message = "Apartment numbers cannot be lower than 1")
    private Integer apartmentsTo;


    private List<String> included;
    private List<Integer> excluded;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Integer getApartmentsFrom() {
        return apartmentsFrom;
    }

    public void setApartmentsFrom(Integer apartmentsFrom) {
        this.apartmentsFrom = apartmentsFrom;
    }

    public Integer getApartmentsTo() {
        return apartmentsTo;
    }

    public void setApartmentsTo(Integer apartmentsTo) {
        this.apartmentsTo = apartmentsTo;
    }

    public List<String> getIncluded() {
        return included;
    }

    public void setIncluded(List<String> included) {
        this.included = included;
    }

    public List<Integer> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<Integer> excluded) {
        this.excluded = excluded;
    }
}
