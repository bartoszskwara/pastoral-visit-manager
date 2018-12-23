package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

abstract class BaseExportService implements FileContentProvider {

    void validateSelectedAddresses(List<SelectedAddress> selectedAddresses) {
        if(isEmpty(selectedAddresses)) {
            throw new IllegalArgumentException("No addresses found");
        }

        selectedAddresses.forEach(this::validateSelectedAddress);
    }

    List<Apartment> sortApartments(List<Apartment> apartments) {
        apartments.sort(ApartmentComparator.byNumber());
        return apartments;
    }

    private void validateSelectedAddress(SelectedAddress selectedAddress) {
        if(selectedAddress == null) {
            throw new IllegalArgumentException("Selected Address not found");
        }

        if(isEmpty(selectedAddress.getSeasons())) {
            throw new IllegalArgumentException("Seasons not found");
        }

        if(selectedAddress.getAddress() == null) {
            throw new IllegalArgumentException("Address not found");
        }

        if(isEmpty(selectedAddress.getAddress().getApartments())) {
            throw new IllegalArgumentException("Apartments not found");
        }
    }
}
