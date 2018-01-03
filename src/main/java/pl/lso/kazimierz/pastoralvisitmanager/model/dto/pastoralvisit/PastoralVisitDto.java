package pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import java.util.Date;

public class PastoralVisitDto {
    private Long id;
    private String value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date date;
    private ApartmentDto apartment;
    private PriestDto priest;

    public PastoralVisitDto(Long id, String value, Date date, ApartmentDto apartment, PriestDto priest) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.apartment = apartment;
        this.priest = priest;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public ApartmentDto getApartment() {
        return apartment;
    }

    public PriestDto getPriest() {
        return priest;
    }
}
