package com.sensocon.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CompanySettings.
 */
@Entity
@Table(name = "company_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companysettings")
public class CompanySettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "default_timeout_seconds")
    private Long defaultTimeoutSeconds;

    @Column(name = "default_suppression_seconds")
    private Long defaultSuppressionSeconds;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefaultTimeoutSeconds() {
        return defaultTimeoutSeconds;
    }

    public CompanySettings defaultTimeoutSeconds(Long defaultTimeoutSeconds) {
        this.defaultTimeoutSeconds = defaultTimeoutSeconds;
        return this;
    }

    public void setDefaultTimeoutSeconds(Long defaultTimeoutSeconds) {
        this.defaultTimeoutSeconds = defaultTimeoutSeconds;
    }

    public Long getDefaultSuppressionSeconds() {
        return defaultSuppressionSeconds;
    }

    public CompanySettings defaultSuppressionSeconds(Long defaultSuppressionSeconds) {
        this.defaultSuppressionSeconds = defaultSuppressionSeconds;
        return this;
    }

    public void setDefaultSuppressionSeconds(Long defaultSuppressionSeconds) {
        this.defaultSuppressionSeconds = defaultSuppressionSeconds;
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
        CompanySettings companySettings = (CompanySettings) o;
        if (companySettings.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companySettings.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanySettings{" +
            "id=" + getId() +
            ", defaultTimeoutSeconds=" + getDefaultTimeoutSeconds() +
            ", defaultSuppressionSeconds=" + getDefaultSuppressionSeconds() +
            "}";
    }
}
