package pl.lso.kazimierz.pastoralvisitmanager.model.dto.address;

import lombok.Data;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;

@Data
public class SelectedAddress {
    private Address address;
    private List<Season> seasons;
    private Integer emptyColumnsCount;
}
