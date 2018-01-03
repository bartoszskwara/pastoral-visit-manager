package pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import java.util.Date;

public class ApartmentHistoryDto {
    private Long id;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date date;
    private ApartmentDto apartment;

    public ApartmentHistoryDto(Long id, String comment, Date date, ApartmentDto apartment) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.apartment = apartment;
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }

    public ApartmentDto getApartment() {
        return apartment;
    }
}
