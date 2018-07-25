package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAddressDto {
    private Long id;
    private String streetName;
    private String blockNumber;
}
