package com.sensocon.core.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CompanySettings entity.
 */
public class CompanySettingsDTO implements Serializable {

    private Long id;

    private Long defaultTimeoutSeconds;

    private Long defaultSuppressionSeconds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDefaultTimeoutSeconds() {
        return defaultTimeoutSeconds;
    }

    public void setDefaultTimeoutSeconds(Long defaultTimeoutSeconds) {
        this.defaultTimeoutSeconds = defaultTimeoutSeconds;
    }

    public Long getDefaultSuppressionSeconds() {
        return defaultSuppressionSeconds;
    }

    public void setDefaultSuppressionSeconds(Long defaultSuppressionSeconds) {
        this.defaultSuppressionSeconds = defaultSuppressionSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanySettingsDTO companySettingsDTO = (CompanySettingsDTO) o;
        if (companySettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companySettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanySettingsDTO{" +
            "id=" + getId() +
            ", defaultTimeoutSeconds=" + getDefaultTimeoutSeconds() +
            ", defaultSuppressionSeconds=" + getDefaultSuppressionSeconds() +
            "}";
    }
}
