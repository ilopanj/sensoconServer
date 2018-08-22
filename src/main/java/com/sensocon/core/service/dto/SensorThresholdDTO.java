package com.sensocon.core.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.sensocon.core.domain.enumeration.ThresholdType;

/**
 * A DTO for the SensorThreshold entity.
 */
public class SensorThresholdDTO implements Serializable {

    private Long id;

    private ThresholdType type;

    private Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ThresholdType getType() {
        return type;
    }

    public void setType(ThresholdType type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SensorThresholdDTO sensorThresholdDTO = (SensorThresholdDTO) o;
        if (sensorThresholdDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorThresholdDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorThresholdDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
