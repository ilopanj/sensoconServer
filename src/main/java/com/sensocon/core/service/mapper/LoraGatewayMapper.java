package com.sensocon.core.service.mapper;

import com.sensocon.core.domain.*;
import com.sensocon.core.service.dto.LoraGatewayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LoraGateway and its DTO LoraGatewayDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface LoraGatewayMapper extends EntityMapper<LoraGatewayDTO, LoraGateway> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    LoraGatewayDTO toDto(LoraGateway loraGateway);

    @Mapping(source = "locationId", target = "location")
    LoraGateway toEntity(LoraGatewayDTO loraGatewayDTO);

    default LoraGateway fromId(Long id) {
        if (id == null) {
            return null;
        }
        LoraGateway loraGateway = new LoraGateway();
        loraGateway.setId(id);
        return loraGateway;
    }
}
