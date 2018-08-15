package pl.lso.kazimierz.pastoralvisitmanager.service.util;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import static org.springframework.util.CollectionUtils.isEmpty;

public class PastoralVisitUtils {

    public static PastoralVisitStatus getPastoralVisitStatus(Apartment apartment, Season season) {
        if(isEmpty(apartment.getPastoralVisits())) {
            return null;
        }
        for(PastoralVisit pastoralVisit : apartment.getPastoralVisits()) {
            if(pastoralVisit.getSeason() != null && pastoralVisit.getSeason().getId().equals(season.getId())) {
                return PastoralVisitStatus.getByStatus(pastoralVisit.getValue());
            }
        }
        return null;
    }
}
