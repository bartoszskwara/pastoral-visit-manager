package pl.lso.kazimierz.pastoralvisitmanager.model.validation.validator;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.validation.annotation.AddressUnique;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressUniqueValidator implements ConstraintValidator<AddressUnique, NewAddress> {

    private AddressRepository addressRepository;

    public AddressUniqueValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void initialize(AddressUnique addressUnique) {
    }

    @Override
    public boolean isValid(NewAddress newAddress, ConstraintValidatorContext ctx) {

        if(newAddress == null) {
            return false;
        }
        if(addressRepository.findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(newAddress.getStreetName(), newAddress.getBlockNumber()) == null) {
            return true;
        }
        else {
            return false;
        }
    }

}