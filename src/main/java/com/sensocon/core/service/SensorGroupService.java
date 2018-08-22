package com.sensocon.core.service;

import com.sensocon.core.service.dto.SensorGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SensorGroup.
 */
public interface SensorGroupService {

    /**
     * Save a sensorGroup.
     *
     * @param sensorGroupDTO the entity to save
     * @return the persisted entity
     */
    SensorGroupDTO save(SensorGroupDTO sensorGroupDTO);

    /**
     * Get all the sensorGroups.
     *
     * @return the list of entities
     */
    List<SensorGroupDTO> findAll();


    /**
     * Get the "id" sensorGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorGroupDTO> findOne(Long id);

    /**
     * Delete the "id" sensorGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sensorGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SensorGroupDTO> search(String query);
}
