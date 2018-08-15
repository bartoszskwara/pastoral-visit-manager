package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectedAddressDto {
    private Long addressId;
    private List<Long> seasons;
    private Integer emptyColumnsCount;
}
