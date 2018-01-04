package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.HasAtLeastOneApartment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class HasAtLeastOneApartmentValidator implements ConstraintValidator<HasAtLeastOneApartment, NewAddress> {

    public HasAtLeastOneApartmentValidator() {
    }

    @Override
    public void initialize(HasAtLeastOneApartment hasAtLeastOneApartment) {
    }

    @Override
    public boolean isValid(NewAddress newAddress, ConstraintValidatorContext ctx) {

        if(newAddress == null) {
            return false;
        }

        Set<String> apartments = new HashSet<>();
        for(int i = newAddress.getApartmentsFrom(); i <= newAddress.getApartmentsTo(); i++) {
            if(!newAddress.getExcluded().contains(i)) {
                apartments.add(String.valueOf(i));
            }
        }
        for(String i : newAddress.getIncluded()) {
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