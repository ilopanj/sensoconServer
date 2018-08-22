package com.sensocon.core.service;

import com.sensocon.core.service.dto.SensorThresholdsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing SensorThresholds.
 */
public interface SensorThresholdsService {

    /**
     * Save a sensorThresholds.
     *
     * @param sensorThresholdsDTO the entity to save
     * @return the persisted entity
     */
    SensorThresholdsDTO save(SensorThresholdsDTO sensorThresholdsDTO);

    /**
     * Get all the sensorThresholds.
     *
     * @return the list of entities
     */
    List<SensorThresholdsDTO> findAll();


    /**
     * Get the "id" sensorThresholds.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorThresholdsDTO> findOne(Long id);

    /**
     * Delete the "id" sensorThresholds.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sensorThresholds corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SensorThresholdsDTO> search(String query);
}
