package pl.lso.kazimierz.pastoralvisitmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a order by lower(a.streetName), lower(a.blockNumber)")
    List<Address> findAll();

    Address findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(String streetName, String blockNumber);

    @Query("select a from Address a " +
            "where lower(concat(a.prefix, ' ', a.streetName, ' ', a.blockNumber)) like concat('%',lower(:name),'%') " +
            "order by a.prefix, a.streetName, a.blockNumber")
    Page<Address> findByNameAsPage(Pageable pageable, @Param("name") String name);

    @Query("select a from Address a where lower(a.streetName) like concat('%',lower(:streetNames),'%')")
    List<Address> findAllByStreetNameContainingIgnoreCase(@Param("streetNames") String streetNames);
}
