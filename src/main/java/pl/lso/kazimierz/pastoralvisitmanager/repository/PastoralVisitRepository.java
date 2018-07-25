package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.CrudRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;

public interface PastoralVisitRepository extends CrudRepository<PastoralVisit, Long> {
}
