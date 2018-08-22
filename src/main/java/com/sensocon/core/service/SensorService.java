package com.sensocon.core.service;

import com.sensocon.core.service.dto.SensorDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Sensor.
 */
public interface SensorService {

    /**
     * Save a sensor.
     *
     * @param sensorDTO the entity to save
     * @return the persisted entity
     */
    SensorDTO save(SensorDTO sensorDTO);

    /**
     * Get all the sensors.
     *
     * @return the list of entities
     */
    List<SensorDTO> findAll();


    /**
     * Get the "id" sensor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SensorDTO> findOne(Long id);

    /**
     * Delete the "id" sensor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the sensor corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<SensorDTO> search(String query);
}
