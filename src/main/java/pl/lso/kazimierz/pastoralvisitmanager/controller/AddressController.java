package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressData;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SimpleAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.AddressMapper;
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
    public ResponseEntity getAllAddresses(@PageableDefault(sort = {"streetName", "blockNumber"}, direction = Sort.Direction.ASC, value = 5) Pageable pageable, @RequestParam("name") String name) {
        Page<SimpleAddressDto> addressDtos = addressService.getAllAddresses(pageable, name).map(AddressMapper::mapSimple);
        return ResponseEntity.ok(addressDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAddressDetails(@PathVariable("id") Long id) {
        Address address = addressService.getAddress(id);
        return ResponseEntity.ok(AddressMapper.map(address));
    }

    @PostMapping({"", "/"})
    public ResponseEntity addNewAddress(@RequestBody @Validated AddressData addressData) {
        Address address = addressService.addNewAddress(addressData);
        return ResponseEntity.ok(AddressMapper.map(address));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity updateAddress(@PathVariable("addressId") Long addressId, @RequestBody @Validated AddressData addressDto) {
        Address address = addressService.updateAddress(addressId, addressDto);
        return ResponseEntity.ok(AddressMapper.map(address));
    }
}
