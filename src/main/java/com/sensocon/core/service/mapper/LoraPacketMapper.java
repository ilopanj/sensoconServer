package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.LoraPacketDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LoraPacket and its DTO LoraPacketDTO.
 */
@Mapper(componentModel = "spring", uses = {SensorDeviceMapper.class})
public interface LoraPacketMapper extends EntityMapper<LoraPacketDTO, LoraPacket> {

    @Mapping(source = "sensorDevice.id", target = "sensorDeviceId")
    @Mapping(source = "sensorDevice.deviceId", target = "sensorDeviceDeviceId")
    LoraPacketDTO toDto(LoraPacket loraPacket);

    @Mapping(source = "sensorDeviceId", target = "sensorDevice")
    LoraPacket toEntity(LoraPacketDTO loraPacketDTO);

    default LoraPacket fromId(Long id) {
        if (id == null) {
            return null;
        }
        LoraPacket loraPacket = new LoraPacket();
        loraPacket.setId(id);
        return loraPacket;
    }
}
