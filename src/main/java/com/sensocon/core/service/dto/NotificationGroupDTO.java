package com.sensocon.core.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the NotificationGroup entity.
 */
public class NotificationGroupDTO implements Serializable {

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

        NotificationGroupDTO notificationGroupDTO = (NotificationGroupDTO) o;
        if (notificationGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationGroupDTO{" +
            "id=" + getId() +
            "}";
    }
}
