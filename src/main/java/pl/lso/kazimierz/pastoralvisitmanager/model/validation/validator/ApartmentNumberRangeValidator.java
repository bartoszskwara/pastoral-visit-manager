package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.ApartmentNumbersRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ApartmentNumberRangeValidator implements ConstraintValidator<ApartmentNumbersRange, NewAddressDto> {

    @Override
    public void initialize(ApartmentNumbersRange apartmentNumbersRange) {
    }

    @Override
    public boolean isValid(NewAddressDto newAddressDto, ConstraintValidatorContext ctx) {

        if(newAddressDto == null) {
            return false;
        }
        if(newAddressDto.getApartmentsFrom() != null && newAddressDto.getApartmentsTo() != null && newAddressDto.getApartmentsFrom().compareTo(newAddressDto.getApartmentsTo()) <= 0) {
            return true;
        }
        else {
            return false;
        }
    }

}