package com.sensocon.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sensor")
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "alerts_enabled")
    private Boolean alertsEnabled;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private SensorDevice sensorDevice;

    @ManyToOne
    @JsonIgnoreProperties("sensors")
    private SensorDevice sensorDevice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sensor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAlertsEnabled() {
        return alertsEnabled;
    }

    public Sensor alertsEnabled(Boolean alertsEnabled) {
        this.alertsEnabled = alertsEnabled;
        return this;
    }

    public void setAlertsEnabled(Boolean alertsEnabled) {
        this.alertsEnabled = alertsEnabled;
    }

    public SensorDevice getSensorDevice() {
        return sensorDevice;
    }

    public Sensor sensorDevice(SensorDevice sensorDevice) {
        this.sensorDevice = sensorDevice;
        return this;
    }

    public void setSensorDevice(SensorDevice sensorDevice) {
        this.sensorDevice = sensorDevice;
    }

    public SensorDevice getSensorDevice() {
        return sensorDevice;
    }

    public Sensor sensorDevice(SensorDevice sensorDevice) {
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
        Sensor sensor = (Sensor) o;
        if (sensor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alertsEnabled='" + isAlertsEnabled() + "'" +
            "}";
    }
}
