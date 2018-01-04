package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity getAllAddressesOrByName(@RequestParam(value = "streetName", defaultValue = "") String streetName,
                                                  Pageable pageable) {
        return new ResponseEntity<>(
                addressService.getAllAddressesOrByName(pageable, streetName)
                        .map(address -> AddressDtoBuilder.getInstance()
                                .withId(address.getId())
                                .withStreetName(address.getStreetName())
                                .withBlockNumber(address.getBlockNumber())
                                .withApartments(null)
                                .build()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAddressDetails(@PathVariable("id") Long id) {
        return new ResponseEntity<>(addressService.getAddressDetails(id), HttpStatus.OK);
    }

}