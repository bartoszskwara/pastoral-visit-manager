package pl.lso.kazimierz.pastoralvisitmanager.model.builder;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.apartment.ApartmentDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressDtoBuilder {

    private Long id;
    private String streetName;
    private String blockNumber;
    private List<ApartmentDto> apartments;

    public AddressDtoBuilder() {
        apartments = new ArrayList<>();
    }

    public static AddressDtoBuilder getInstance() {
        return new AddressDtoBuilder();
    }

    public AddressDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AddressDtoBuilder withStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public AddressDtoBuilder withBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public AddressDtoBuilder withApartments(List<ApartmentDto> apartments) {
        this.apartments = apartments;
        return this;
    }

    public AddressDtoBuilder withApartment(ApartmentDto apartment) {
        if(this.apartments == null) {
            this.apartments = new ArrayList<>();
        }
        this.apartments.add(apartment);
        return this;
    }

    public static AddressDto buildFromEntity(Address address) {
        List<ApartmentDto> apartments = address.getApartments().stream()
                .map(a -> ApartmentDtoBuilder.getInstance()
                        .withId(a.getId())
                        .withNumber(a.getNumber())
                        .withPastoralVisits(null)
                        .withApartmentHistories(null)
                        .build())
                .collect(Collectors.toList());

        return AddressDtoBuilder.getInstance()
                .withId(address.getId())
                .withStreetName(address.getStreetName())
                .withBlockNumber(address.getBlockNumber())
                .withApartments(apartments)
                .build();
    }

    public AddressDto build() {
        return new AddressDto(
                this.id,
                this.streetName,
                this.blockNumber,
                this.apartments
        );
    }
}
