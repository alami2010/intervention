package com.ydahar.jbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TierData.
 */
@Entity
@Table(name = "tier_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TierData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tier")
    @JsonIgnoreProperties(value = { "tier" }, allowSetters = true)
    private Set<RoomData> roomData = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tierData" }, allowSetters = true)
    private FloorData floor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TierData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TierData name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoomData> getRoomData() {
        return this.roomData;
    }

    public void setRoomData(Set<RoomData> roomData) {
        if (this.roomData != null) {
            this.roomData.forEach(i -> i.setTier(null));
        }
        if (roomData != null) {
            roomData.forEach(i -> i.setTier(this));
        }
        this.roomData = roomData;
    }

    public TierData roomData(Set<RoomData> roomData) {
        this.setRoomData(roomData);
        return this;
    }

    public TierData addRoomData(RoomData roomData) {
        this.roomData.add(roomData);
        roomData.setTier(this);
        return this;
    }

    public TierData removeRoomData(RoomData roomData) {
        this.roomData.remove(roomData);
        roomData.setTier(null);
        return this;
    }

    public FloorData getFloor() {
        return this.floor;
    }

    public void setFloor(FloorData floorData) {
        this.floor = floorData;
    }

    public TierData floor(FloorData floorData) {
        this.setFloor(floorData);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TierData)) {
            return false;
        }
        return id != null && id.equals(((TierData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TierData{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
