package com.sensocon.core.service;

import com.sensocon.core.service.dto.LoraGatewayDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing LoraGateway.
 */
public interface LoraGatewayService {

    /**
     * Save a loraGateway.
     *
     * @param loraGatewayDTO the entity to save
     * @return the persisted entity
     */
    LoraGatewayDTO save(LoraGatewayDTO loraGatewayDTO);

    /**
     * Get all the loraGateways.
     *
     * @return the list of entities
     */
    List<LoraGatewayDTO> findAll();


    /**
     * Get the "id" loraGateway.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LoraGatewayDTO> findOne(Long id);

    /**
     * Delete the "id" loraGateway.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the loraGateway corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<LoraGatewayDTO> search(String query);
}
