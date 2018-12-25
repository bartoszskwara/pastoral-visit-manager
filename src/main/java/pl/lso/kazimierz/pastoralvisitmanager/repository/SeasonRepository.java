package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    @Query("SELECT s FROM Season s WHERE s.name IN :names")
    List<Season> findAllByName(@Param("names") List<String> names);

    @Query("SELECT s FROM Season s WHERE s.name = :name")
    Season findByName(@Param("name") String name);

    @Query("select s from Season s " +
            "where s.current <> true " +
            "order by s.endDate desc")
    List<Season> findNotCurrentOrderedByEndDate();
}
