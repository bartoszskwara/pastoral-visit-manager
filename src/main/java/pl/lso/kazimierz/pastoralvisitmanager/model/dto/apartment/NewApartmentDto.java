package pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment;

import javax.validation.constraints.NotNull;

public class NewApartmentDto {

    @NotNull(message = "Number cannot be null")
    private String number;

    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
}
