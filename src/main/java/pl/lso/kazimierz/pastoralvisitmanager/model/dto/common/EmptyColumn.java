package pl.lso.kazimierz.pastoralvisitmanager.model.dto.common;

import lombok.Builder;
import lombok.Data;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;

@Data
@Builder
public class EmptyColumn {
    private Long id;
    private Address address;
    private String name;
}
