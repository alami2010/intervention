package com.ydahar.jbd.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.Tier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TierDTO implements Serializable {

    private Long id;

    private String name;

    private boolean checked;

    private List<RoomDTO> rooms;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TierDTO)) {
            return false;
        }

        TierDTO tierDTO = (TierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tierDTO.id);
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
             "}";
    }
}
