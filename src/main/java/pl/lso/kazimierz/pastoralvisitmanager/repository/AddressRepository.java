package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(String streetName, String blockNumber);
}
