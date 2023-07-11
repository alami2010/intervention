package com.ydahar.jbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A FloorData.
 */
@Entity
@Table(name = "floor_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloorData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "floor")
    @JsonIgnoreProperties(value = { "roomData", "floor" }, allowSetters = true)
    private Set<TierData> tierData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FloorData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FloorData name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TierData> getTierData() {
        return this.tierData;
    }

    public void setTierData(Set<TierData> tierData) {
        if (this.tierData != null) {
            this.tierData.forEach(i -> i.setFloor(null));
        }
        if (tierData != null) {
            tierData.forEach(i -> i.setFloor(this));
        }
        this.tierData = tierData;
    }

    public FloorData tierData(Set<TierData> tierData) {
        this.setTierData(tierData);
        return this;
    }

    public FloorData addTierData(TierData tierData) {
        this.tierData.add(tierData);
        tierData.setFloor(this);
        return this;
    }

    public FloorData removeTierData(TierData tierData) {
        this.tierData.remove(tierData);
        tierData.setFloor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloorData)) {
            return false;
        }
        return id != null && id.equals(((FloorData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloorData{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
