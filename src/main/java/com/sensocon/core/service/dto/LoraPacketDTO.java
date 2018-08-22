package com.sensocon.core.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LoraPacket entity.
 */
public class LoraPacketDTO implements Serializable {

    private Long id;

    private Double rssi;

    private Double batteryLevel;

    private Instant timestamp;

    private Double temperature;

    private Double pressure;

    private Long sensorDeviceId;

    private String sensorDeviceDeviceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRssi() {
        return rssi;
    }

    public void setRssi(Double rssi) {
        this.rssi = rssi;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoraPacketDTO loraPacketDTO = (LoraPacketDTO) o;
        if (loraPacketDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loraPacketDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoraPacketDTO{" +
            "id=" + getId() +
            ", rssi=" + getRssi() +
            ", batteryLevel=" + getBatteryLevel() +
            ", timestamp='" + getTimestamp() + "'" +
            ", temperature=" + getTemperature() +
            ", pressure=" + getPressure() +
            ", sensorDevice=" + getSensorDeviceId() +
            ", sensorDevice='" + getSensorDeviceDeviceId() + "'" +
            "}";
    }
}
