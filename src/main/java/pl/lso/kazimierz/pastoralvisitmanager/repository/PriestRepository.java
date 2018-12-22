package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;

public interface PriestRepository extends JpaRepository<Priest, Long> {

    @Query("select p from Priest p where lower(p.name) = lower(:name)")
    Priest findByNameIgnoreCase(@Param("name") String name);
}
