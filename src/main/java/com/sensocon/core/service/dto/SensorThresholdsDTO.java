package com.sensocon.core.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SensorThresholds entity.
 */
public class SensorThresholdsDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SensorThresholdsDTO sensorThresholdsDTO = (SensorThresholdsDTO) o;
        if (sensorThresholdsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorThresholdsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorThresholdsDTO{" +
            "id=" + getId() +
            "}";
    }
}
