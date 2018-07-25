package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.CrudRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.ApartmentHistory;

public interface ApartmentHistoryRepository extends CrudRepository<ApartmentHistory, Long> {
}
