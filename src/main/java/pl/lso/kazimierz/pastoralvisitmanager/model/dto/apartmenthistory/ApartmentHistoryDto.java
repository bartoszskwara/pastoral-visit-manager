package pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartmenthistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApartmentHistoryDto {
    private Long id;
    private String comment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date date;
    private Long apartmentId;
}
