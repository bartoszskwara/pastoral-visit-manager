package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;

public class PastoralVisitMapper {

    public static PastoralVisitDto map(PastoralVisit pastoralVisit) {
        return PastoralVisitDto.builder()
                .id(pastoralVisit.getId())
                .date(pastoralVisit.getDate())
                .value(pastoralVisit.getValue())
                .priestId(pastoralVisit.getPriest().getId())
                .apartmentId(pastoralVisit.getApartment().getId())
                .build();
    }
}
