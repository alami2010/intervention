package com.ydahar.jbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ydahar.jbd.domain.enumeration.TypeIntervention;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Intervention.
 */
@Entity
@Table(name = "intervention")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Intervention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeIntervention type;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "finish")
    private LocalDateTime finish;

    @Column(name = "raison")
    private String raison;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "intervention")
    @JsonIgnoreProperties(value = { "tiers", "intervention" }, allowSetters = true)
    private Set<Floor> floors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intervention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeIntervention getType() {
        return this.type;
    }

    public Intervention type(TypeIntervention type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeIntervention type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public Intervention start(LocalDateTime start) {
        this.setStart(start);
        return this;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return this.finish;
    }

    public Intervention finish(LocalDateTime finish) {
        this.setFinish(finish);
        return this;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    public String getRaison() {
        return this.raison;
    }

    public Intervention raison(String raison) {
        this.setRaison(raison);
        return this;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getUnitNumber() {
        return this.unitNumber;
    }

    public Intervention unitNumber(String unitNumber) {
        this.setUnitNumber(unitNumber);
        return this;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Intervention creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Floor> getFloors() {
        return this.floors;
    }

    public void setFloors(Set<Floor> floors) {
        if (this.floors != null) {
            this.floors.forEach(i -> i.setIntervention(null));
        }
        if (floors != null) {
            floors.forEach(i -> i.setIntervention(this));
        }
        this.floors = floors;
    }

    public Intervention floors(Set<Floor> floors) {
        this.setFloors(floors);
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Intervention addFloor(Floor floor) {
        this.floors.add(floor);
        floor.setIntervention(this);
        return this;
    }

    public Intervention removeFloor(Floor floor) {
        this.floors.remove(floor);
        floor.setIntervention(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intervention)) {
            return false;
        }
        return id != null && id.equals(((Intervention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intervention{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", start='" + getStart() + "'" +
            ", finish='" + getFinish() + "'" +
            ", raison='" + getRaison() + "'" +
            ", unitNumber='" + getUnitNumber() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
