package pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

//TODO validation: date not in the future
public class NewPastoralVisit {

    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss +Z")
    private Date date;

    @NotNull(message = "Value cannot be null")
    @Size(min=1, message = "Value cannot be blank")
    private String value;

    @NotNull(message = "Apartment ID cannot be null")
    private Long apartmentId;

    @NotNull(message = "Priest ID cannot be null")
    private Long priestId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Long getPriestId() {
        return priestId;
    }

    public void setPriestId(Long priestId) {
        this.priestId = priestId;
    }
}
