package pl.lso.kazimierz.pastoralvisitmanager.model.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(schema = "public", name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "street_name")
    private String streetName;

    @Column(nullable = false, name = "block_number")
    private String blockNumber;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    private List<Apartment> apartments = new ArrayList<>();
}
