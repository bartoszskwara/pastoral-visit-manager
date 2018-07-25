package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.CrudRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

public interface ApartmentRepository extends CrudRepository<Apartment, Long> {
}
