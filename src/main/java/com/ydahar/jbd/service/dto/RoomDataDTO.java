package com.ydahar.jbd.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.RoomData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomDataDTO implements Serializable {

    private Long id;

    private String name;

    private TierDataDTO tier;

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

    public TierDataDTO getTier() {
        return tier;
    }

    public void setTier(TierDataDTO tier) {
        this.tier = tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDataDTO)) {
            return false;
        }

        RoomDataDTO roomDataDTO = (RoomDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDataDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tier=" + getTier() +
            "}";
    }
}
