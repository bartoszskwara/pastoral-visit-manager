package pl.lso.kazimierz.pastoralvisitmanager.service.util;

import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.Date;

class SeasonUtils {

    static boolean seasonIncludesDate(Season season, Date date) {
        if(date == null) {
            return false;
        }
        return date.after(season.getStartDate()) && date.before(season.getEndDate());
    }
}
