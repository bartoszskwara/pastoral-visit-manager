package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.season.SeasonDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class SeasonMapper {

    public static List<SeasonDto> mapSeasonDtos(List<Season> seasons) {
        if(isEmpty(seasons)) {
            return emptyList();
        }
        return seasons.stream().map(SeasonMapper::mapToDto).collect(Collectors.toList());
    }

    public static List<Season> mapSeasons(List<SeasonDto> seasonDtos) {
        if(isEmpty(seasonDtos)) {
            return emptyList();
        }
        return seasonDtos.stream().map(SeasonMapper::mapFromDto).collect(Collectors.toList());
    }

    private static SeasonDto mapToDto(Season season) {
        SeasonDto seasonDto = new SeasonDto();
        seasonDto.setId(season.getId());
        seasonDto.setName(season.getName());
        seasonDto.setStart(season.getStartDate());
        seasonDto.setEnd(season.getEndDate());
        seasonDto.setCurrent(season.isCurrent());
        return seasonDto;
    }

    private static Season mapFromDto(SeasonDto seasonDto) {
        return Season.builder()
                .name(seasonDto.getName())
                .startDate(seasonDto.getStart())
                .endDate(seasonDto.getEnd())
                .current(seasonDto.isCurrent())
                .build();
    }
}
