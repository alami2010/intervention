package com.ydahar.jbd.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.FloorData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloorDataDTO implements Serializable {

    private Long id;

    private String name;

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
        if (!(o instanceof FloorDataDTO)) {
            return false;
        }

        FloorDataDTO floorDataDTO = (FloorDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, floorDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloorDataDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
