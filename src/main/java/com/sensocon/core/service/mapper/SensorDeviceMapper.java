package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.SensorDeviceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SensorDevice and its DTO SensorDeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, CompanyMapper.class, NotificationGroupMapper.class})
public interface SensorDeviceMapper extends EntityMapper<SensorDeviceDTO, SensorDevice> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "notificationGroup.id", target = "notificationGroupId")
    SensorDeviceDTO toDto(SensorDevice sensorDevice);

    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "sensors", ignore = true)
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "notificationGroupId", target = "notificationGroup")
    SensorDevice toEntity(SensorDeviceDTO sensorDeviceDTO);

    default SensorDevice fromId(Long id) {
        if (id == null) {
            return null;
        }
        SensorDevice sensorDevice = new SensorDevice();
        sensorDevice.setId(id);
        return sensorDevice;
    }
}
