package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private Long id;
    private String streetName;
    private String blockNumber;
    private List<ApartmentDto> apartments;
}
