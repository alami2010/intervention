package com.ydahar.jbd.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.TierData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TierDataDTO implements Serializable {

    private Long id;

    private String name;

    private FloorDataDTO floor;

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

    public FloorDataDTO getFloor() {
        return floor;
    }

    public void setFloor(FloorDataDTO floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TierDataDTO)) {
            return false;
        }

        TierDataDTO tierDataDTO = (TierDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tierDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TierDataDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", floor=" + getFloor() +
            "}";
    }
}
