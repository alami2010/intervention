package com.ydahar.jbd.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.Floor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FloorDTO implements Serializable {

    private Long id;

    private String name;

    private boolean checked;

    private List<TierDTO> tiers;

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
        if (!(o instanceof FloorDTO)) {
            return false;
        }

        FloorDTO floorDTO = (FloorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, floorDTO.id);
    }

    public List<TierDTO> getTiers() {
        return tiers;
    }

    public void setTiers(List<TierDTO> tiers) {
        this.tiers = tiers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FloorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
             "}";
    }
}
