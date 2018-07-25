package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.HasAtLeastOneApartment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class HasAtLeastOneApartmentValidator implements ConstraintValidator<HasAtLeastOneApartment, NewAddressDto> {

    public HasAtLeastOneApartmentValidator() {
    }

    @Override
    public void initialize(HasAtLeastOneApartment hasAtLeastOneApartment) {
    }

    @Override
    public boolean isValid(NewAddressDto newAddressDto, ConstraintValidatorContext ctx) {

        if(newAddressDto == null) {
            return false;
        }

        Set<String> apartments = new HashSet<>();
        for(int i = newAddressDto.getApartmentsFrom(); i <= newAddressDto.getApartmentsTo(); i++) {
            if(!newAddressDto.getExcluded().contains(i)) {
                apartments.add(String.valueOf(i));
            }
        }
        for(String i : newAddressDto.getIncluded()) {
            apartments.add(String.valueOf(i));
        }

        if(apartments.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

}