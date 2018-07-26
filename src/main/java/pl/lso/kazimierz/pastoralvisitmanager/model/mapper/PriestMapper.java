package pl.lso.kazimierz.pastoralvisitmanager.model.mapper;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SimpleAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.priest.PriestDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

public class PriestMapper {
    public static PriestDto map(Priest priest) {
        return PriestDto.builder()
                .id(priest.getId())
                .name(priest.getName())
                .build();
    }
}
