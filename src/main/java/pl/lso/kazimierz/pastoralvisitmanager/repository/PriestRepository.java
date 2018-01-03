package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

public interface PriestRepository extends PagingAndSortingRepository<Priest, Long> {
}
