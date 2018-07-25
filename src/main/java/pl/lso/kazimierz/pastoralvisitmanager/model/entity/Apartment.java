package pl.lso.kazimierz.pastoralvisitmanager.model.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(schema = "public", name = "apartment")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "number")
    private String number;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<ApartmentHistory> apartmentHistories = new ArrayList<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private List<PastoralVisit> pastoralVisits = new ArrayList<>();
}
