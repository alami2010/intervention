package com.ydahar.jbd.service.dto;

import com.ydahar.jbd.domain.enumeration.TypeIntervention;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.ydahar.jbd.domain.Intervention} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionDTO implements Serializable {

    private Long id;

    private TypeIntervention type;

    private LocalDateTime start;

    private LocalDateTime finish;

    private String raison;

    private String email;

    private String unitNumber;

    private LocalDate creationDate;

    private List<FloorDTO> floors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeIntervention getType() {
        return type;
    }

    public void setType(TypeIntervention type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterventionDTO)) {
            return false;
        }

        InterventionDTO interventionDTO = (InterventionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interventionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public List<FloorDTO> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorDTO> floors) {
        this.floors = floors;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionDTO{" +
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
