package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.AddressUnique;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressUniqueValidator implements ConstraintValidator<AddressUnique, NewAddressDto> {

    private AddressRepository addressRepository;

    public AddressUniqueValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void initialize(AddressUnique addressUnique) {
    }

    @Override
    public boolean isValid(NewAddressDto newAddressDto, ConstraintValidatorContext ctx) {

        if(newAddressDto == null) {
            return false;
        }
        if(addressRepository.findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(newAddressDto.getStreetName(), newAddressDto.getBlockNumber()) == null) {
            return true;
        }
        else {
            return false;
        }
    }

}