package pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PastoralVisitDto {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss Z")
    private Date date;

    @NotNull(message = "Value cannot be null")
    @Size(min=1, message = "Value cannot be blank")
    private String value;

    @NotNull(message = "Apartment ID cannot be null")
    private Long apartmentId;

    @NotNull(message = "Priest ID cannot be null")
    private Long priestId;

    @NotNull(message = "Season ID cannot be null")
    private Long seasonId;
}
