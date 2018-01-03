package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.ApartmentHistory;

public interface ApartmentHistoryRepository extends PagingAndSortingRepository<ApartmentHistory, Long> {
}
