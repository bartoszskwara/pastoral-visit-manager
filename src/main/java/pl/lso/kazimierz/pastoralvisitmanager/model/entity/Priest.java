package pl.lso.kazimierz.pastoralvisitmanager.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "public", name = "priest")
public class Priest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @OneToMany(mappedBy = "priest", cascade = CascadeType.ALL)
    private Set<PastoralVisit> pastoralVisits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PastoralVisit> getPastoralVisits() {
        return pastoralVisits;
    }

    public void setPastoralVisits(Set<PastoralVisit> pastoralVisits) {
        this.pastoralVisits = pastoralVisits;
    }
}
