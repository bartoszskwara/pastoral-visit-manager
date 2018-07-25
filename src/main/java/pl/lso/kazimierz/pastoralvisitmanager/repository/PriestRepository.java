package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.CrudRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

public interface PriestRepository extends CrudRepository<Priest, Long> {
}
