package com.sensocon.core.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Sensor entity.
 */
public class SensorDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean alertsEnabled;

    private Long sensorDeviceId;

    private String sensorDeviceDeviceId;

    private Long sensorDeviceId;

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

    public Boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    public void setAlertsEnabled(Boolean alertsEnabled) {
        this.alertsEnabled = alertsEnabled;
    }

    public Long getSensorDeviceId() {
        return sensorDeviceId;
    }

    public void setSensorDeviceId(Long sensorDeviceId) {
        this.sensorDeviceId = sensorDeviceId;
    }

    public String getSensorDeviceDeviceId() {
        return sensorDeviceDeviceId;
    }

    public void setSensorDeviceDeviceId(String sensorDeviceDeviceId) {
        this.sensorDeviceDeviceId = sensorDeviceDeviceId;
    }

    public Long getSensorDeviceId() {
        return sensorDeviceId;
    }

    public void setSensorDeviceId(Long sensorDeviceId) {
        this.sensorDeviceId = sensorDeviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SensorDTO sensorDTO = (SensorDTO) o;
        if (sensorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alertsEnabled='" + isAlertsEnabled() + "'" +
            ", sensorDevice=" + getSensorDeviceId() +
            ", sensorDevice='" + getSensorDeviceDeviceId() + "'" +
            ", sensorDevice=" + getSensorDeviceId() +
            "}";
    }
}
