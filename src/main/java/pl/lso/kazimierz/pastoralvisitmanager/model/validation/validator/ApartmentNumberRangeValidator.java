package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.ApartmentNumbersRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ApartmentNumberRangeValidator implements ConstraintValidator<ApartmentNumbersRange, NewAddress> {

    @Override
    public void initialize(ApartmentNumbersRange apartmentNumbersRange) {
    }

    @Override
    public boolean isValid(NewAddress newAddress, ConstraintValidatorContext ctx) {

        if(newAddress == null) {
            return false;
        }
        if(newAddress.getApartmentsFrom().compareTo(newAddress.getApartmentsTo()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

}