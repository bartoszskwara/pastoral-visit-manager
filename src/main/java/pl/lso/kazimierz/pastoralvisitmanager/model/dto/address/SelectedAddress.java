package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectedAddress {
    private Address address;
    private List<Season> seasons;
}
