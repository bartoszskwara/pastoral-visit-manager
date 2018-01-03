package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;

public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
}
