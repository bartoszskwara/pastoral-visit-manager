package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
