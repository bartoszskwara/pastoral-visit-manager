package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NotNull(message = "Address data not found")
public class AddressData {

    @NotNull(message = "Street name cannot be null")
    @Size(min=1, message = "Street name cannot be blank")
    private String streetName;

    @NotNull(message = "Block number cannot be null")
    @Size(min=1, message = "Block number cannot be blank")
    private String blockNumber;

    @NotEmpty(message = "Address should have at least one apartment")
    private List<String> apartments;
}

