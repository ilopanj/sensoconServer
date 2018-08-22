package com.sensocon.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.sensocon.core.domain.enumeration.ThresholdType;

/**
 * A SensorThreshold.
 */
@Entity
@Table(name = "sensor_threshold")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sensorthreshold")
public class SensorThreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ThresholdType type;

    @Column(name = "jhi_value")
    private Double value;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ThresholdType getType() {
        return type;
    }

    public SensorThreshold type(ThresholdType type) {
        this.type = type;
        return this;
    }

    public void setType(ThresholdType type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public SensorThreshold value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
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
        SensorThreshold sensorThreshold = (SensorThreshold) o;
        if (sensorThreshold.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensorThreshold.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SensorThreshold{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
