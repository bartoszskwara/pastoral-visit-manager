package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.common.EmptyColumnDto;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectedAddressDto {
    private Long addressId;
    private List<Long> seasons;
    private List<EmptyColumnDto> emptyColumns;
}
