package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

import java.util.List;

public interface ApartmentRepository extends CrudRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a WHERE a.address.id = :addressId")
    List<Apartment> findByAddressId(@Param("addressId") Long addressId);

    @Query("SELECT a FROM Apartment a WHERE a.address.id = :addressId and a.number = :number")
    Apartment findByAddressIdAndNumber(@Param("addressId") Long addressId, @Param("number") String number);
}
