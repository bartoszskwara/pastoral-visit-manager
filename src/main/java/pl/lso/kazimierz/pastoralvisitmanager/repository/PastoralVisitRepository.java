package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;

public interface PastoralVisitRepository extends PagingAndSortingRepository<PastoralVisit, Long> {
}
