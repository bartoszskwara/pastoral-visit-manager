package pl.lso.kazimierz.pastoralvisitmanager.model.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmptyColumnDto {
    private Long id;
    private Long addressId;
    private String name;
}
