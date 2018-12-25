package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;

import java.util.List;

public interface PastoralVisitRepository extends CrudRepository<PastoralVisit, Long> {
    @Query("select p from PastoralVisit p " +
            "inner join Season s " +
            "on p.season.id = s.id " +
            "where p.apartment.id = :apartmentId " +
            "and s.id = :seasonId")
    PastoralVisit findByApartmentIdAndSeasonId(@Param("apartmentId") Long apartmentId, @Param("seasonId") Long seasonId);

    @Query("select count(pv.id) from PastoralVisit pv " +
            "where pv.apartment.address.id = :addressId " +
            "and pv.season.id = :seasonId " +
            "and pv.value in (:values)")
    long countPastoralVisitByAddressAndSeasonAndValue(@Param("addressId") long addressId, @Param("seasonId") long seasonId, @Param("values") List<String> values);
}
