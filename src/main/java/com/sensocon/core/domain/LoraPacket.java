package com.sensocon.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A LoraPacket.
 */
@Entity
@Table(name = "lora_packet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lorapacket")
public class LoraPacket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rssi")
    private Double rssi;

    @Column(name = "battery_level")
    private Double batteryLevel;

    @Column(name = "jhi_timestamp")
    private Instant timestamp;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "pressure")
    private Double pressure;

    @ManyToOne
    @JsonIgnoreProperties("")
    private SensorDevice sensorDevice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRssi() {
        return rssi;
    }

    public LoraPacket rssi(Double rssi) {
        this.rssi = rssi;
        return this;
    }

    public void setRssi(Double rssi) {
        this.rssi = rssi;
    }

    public Double getBatteryLevel() {
        return batteryLevel;
    }

    public LoraPacket batteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
        return this;
    }

    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public LoraPacket timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getTemperature() {
        return temperature;
    }

    public LoraPacket temperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public LoraPacket pressure(Double pressure) {
        this.pressure = pressure;
        return this;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public SensorDevice getSensorDevice() {
        return sensorDevice;
    }

    public LoraPacket sensorDevice(SensorDevice sensorDevice) {
        this.sensorDevice = sensorDevice;
        return this;
    }

    public void setSensorDevice(SensorDevice sensorDevice) {
        this.sensorDevice = sensorDevice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoraPacket loraPacket = (LoraPacket) o;
        if (loraPacket.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loraPacket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoraPacket{" +
            "id=" + getId() +
            ", rssi=" + getRssi() +
            ", batteryLevel=" + getBatteryLevel() +
            ", timestamp='" + getTimestamp() + "'" +
            ", temperature=" + getTemperature() +
            ", pressure=" + getPressure() +
            "}";
    }
}
