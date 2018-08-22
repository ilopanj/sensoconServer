package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.NotificationGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NotificationGroup and its DTO NotificationGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificationGroupMapper extends EntityMapper<NotificationGroupDTO, NotificationGroup> {


    @Mapping(target = "sensorDevices", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    NotificationGroup toEntity(NotificationGroupDTO notificationGroupDTO);

    default NotificationGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationGroup notificationGroup = new NotificationGroup();
        notificationGroup.setId(id);
        return notificationGroup;
    }
}
