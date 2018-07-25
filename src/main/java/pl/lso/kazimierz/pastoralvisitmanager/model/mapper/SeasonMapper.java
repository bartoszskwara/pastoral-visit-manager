package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.season.SeasonDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;
import java.util.stream.Collectors;

public class SeasonMapper {

    public static List<SeasonDto> map(List<Season> seasons) {
        return seasons.stream().map(SeasonMapper::map).collect(Collectors.toList());
    }

    private static SeasonDto map(Season season) {
        return SeasonDto.builder()
                .id(season.getId())
                .name(season.getName())
                .start(season.getStartDate())
                .end(season.getEndDate())
                .current(season.isCurrent())
                .build();
    }
}
