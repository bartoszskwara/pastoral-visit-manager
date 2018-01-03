package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long> {
}
