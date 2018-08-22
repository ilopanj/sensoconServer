package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.SensorGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SensorGroup and its DTO SensorGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SensorGroupMapper extends EntityMapper<SensorGroupDTO, SensorGroup> {



    default SensorGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        SensorGroup sensorGroup = new SensorGroup();
        sensorGroup.setId(id);
        return sensorGroup;
    }
}
