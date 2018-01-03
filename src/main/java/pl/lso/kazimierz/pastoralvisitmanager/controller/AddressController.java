package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.builder.AddressDtoBuilder;
import pl.lso.kazimierz.pastoralvisitmanager.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity getAllAddresses(Pageable pageable) {
        return new ResponseEntity<>(
                addressService.getAllAddresses(pageable)
                        .map(address -> AddressDtoBuilder.getInstance()
                                .withId(address.getId())
                                .withStreetName(address.getStreetName())
                                .withBlockNumber(address.getBlockNumber())
                                .withApartments(null)
                                .build()),
                HttpStatus.OK);
    }

}
