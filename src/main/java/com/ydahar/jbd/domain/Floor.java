package com.ydahar.jbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Floor.
 */
@Entity
@Table(name = "floor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "floor")
    @JsonIgnoreProperties(value = { "rooms", "floor" }, allowSetters = true)
    private Set<Tier> tiers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "floors" }, allowSetters = true)
    private Intervention intervention;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Floor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Floor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tier> getTiers() {
        return this.tiers;
    }

    public void setTiers(Set<Tier> tiers) {
        if (this.tiers != null) {
            this.tiers.forEach(i -> i.setFloor(null));
        }
        if (tiers != null) {
            tiers.forEach(i -> i.setFloor(this));
        }
        this.tiers = tiers;
    }

    public Floor tiers(Set<Tier> tiers) {
        this.setTiers(tiers);
        return this;
    }

    public Floor addTier(Tier tier) {
        this.tiers.add(tier);
        tier.setFloor(this);
        return this;
    }

    public Floor removeTier(Tier tier) {
        this.tiers.remove(tier);
        tier.setFloor(null);
        return this;
    }

    public Intervention getIntervention() {
        return this.intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public Floor intervention(Intervention intervention) {
        this.setIntervention(intervention);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Floor)) {
            return false;
        }
        return id != null && id.equals(((Floor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Floor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
