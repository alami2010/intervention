package com.ydahar.jbd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Tier.
 */
@Entity
@Table(name = "tier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "checked")
    private boolean checked;

    @OneToMany(mappedBy = "tier")
    @JsonIgnoreProperties(value = { "tier" }, allowSetters = true)
    private Set<Room> rooms = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tiers", "intervention" }, allowSetters = true)
    private Floor floor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tier name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(Set<Room> rooms) {
        if (this.rooms != null) {
            this.rooms.forEach(i -> i.setTier(null));
        }
        if (rooms != null) {
            rooms.forEach(i -> i.setTier(this));
        }
        this.rooms = rooms;
    }

    public Tier rooms(Set<Room> rooms) {
        this.setRooms(rooms);
        return this;
    }

    public Tier addRoom(Room room) {
        this.rooms.add(room);
        room.setTier(this);
        return this;
    }

    public Tier removeRoom(Room room) {
        this.rooms.remove(room);
        room.setTier(null);
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Floor getFloor() {
        return this.floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Tier floor(Floor floor) {
        this.setFloor(floor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tier)) {
            return false;
        }
        return id != null && id.equals(((Tier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tier{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
