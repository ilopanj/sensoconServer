package com.sensocon.core.service;

import com.sensocon.core.service.dto.SensorThresholdDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SensorThreshold.
 */
public interface SensorThresholdService {

    /**
     * Save a sensorThreshold.
     *
     * @param sensorThresholdDTO the entity to save
     * @return the persisted entity
     */
    SensorThresholdDTO save(SensorThresholdDTO sensorThresholdDTO);

    /**
     * Get all the sensorThresholds.
     *
     * @return the list of entities
     */
    List<SensorThresholdDTO> findAll();


    /**
     * Get the "id" sensorThreshold.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorThresholdDTO> findOne(Long id);

    /**
     * Delete the "id" sensorThreshold.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sensorThreshold corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SensorThresholdDTO> search(String query);
}
