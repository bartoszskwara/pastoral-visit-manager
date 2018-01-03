package pl.lso.kazimierz.pastoralvisitmanager.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "apartment")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "number")
    private Integer number;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Set<ApartmentHistory> apartmentHistories = new HashSet<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Set<PastoralVisit> pastoralVisits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<ApartmentHistory> getApartmentHistories() {
        return apartmentHistories;
    }

    public void setApartmentHistories(Set<ApartmentHistory> apartmentHistories) {
        this.apartmentHistories = apartmentHistories;
    }

    public Set<PastoralVisit> getPastoralVisits() {
        return pastoralVisits;
    }

    public void setPastoralVisits(Set<PastoralVisit> pastoralVisits) {
        this.pastoralVisits = pastoralVisits;
    }
}
