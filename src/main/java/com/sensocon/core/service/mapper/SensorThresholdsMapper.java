package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.SensorThresholdsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SensorThresholds and its DTO SensorThresholdsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SensorThresholdsMapper extends EntityMapper<SensorThresholdsDTO, SensorThresholds> {



    default SensorThresholds fromId(Long id) {
        if (id == null) {
            return null;
        }
        SensorThresholds sensorThresholds = new SensorThresholds();
        sensorThresholds.setId(id);
        return sensorThresholds;
    }
}
