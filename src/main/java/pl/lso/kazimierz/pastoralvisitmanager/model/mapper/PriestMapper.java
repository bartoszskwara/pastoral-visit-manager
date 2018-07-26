package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

public class PriestMapper {
    public static PriestDto map(Priest priest) {
        return PriestDto.builder()
                .id(priest.getId())
                .name(priest.getName())
                .build();
    }
}
