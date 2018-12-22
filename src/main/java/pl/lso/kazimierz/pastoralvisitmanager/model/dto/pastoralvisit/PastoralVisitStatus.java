package pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit;

import org.apache.commons.lang3.StringUtils;

public enum PastoralVisitStatus {
    completed("+"), refused("-"), absent("?"), individually("ind"), not_requested("x");

    private String description;
    private String status;

    PastoralVisitStatus(String status) {
        this.status = status;
    }

    PastoralVisitStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public void withDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }

    public static PastoralVisitStatus getByStatus(String status) {
        for(PastoralVisitStatus visitStatus : values()) {
            if(StringUtils.equalsIgnoreCase(status, visitStatus.status)) {
                return visitStatus;
            }
        }
        return null;
    }

    public static PastoralVisitStatus getByName(String name) {
        for(PastoralVisitStatus status : values()) {
            if(StringUtils.equalsIgnoreCase(name, status.name())) {
                return status;
            }
        }
        return null;
    }
}
