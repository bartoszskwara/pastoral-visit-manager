package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

public interface PriestRepository extends JpaRepository<Priest, Long> {

}
