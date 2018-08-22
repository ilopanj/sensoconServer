package com.sensocon.core.service.impl;

import com.sensocon.core.service.SensorService;
import com.sensocon.core.domain.Sensor;
import com.sensocon.core.repository.SensorRepository;
import com.sensocon.core.repository.search.SensorSearchRepository;
import com.sensocon.core.service.dto.SensorDTO;
import com.sensocon.core.service.mapper.SensorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Sensor.
 */
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

    private final Logger log = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    private final SensorSearchRepository sensorSearchRepository;

    public SensorServiceImpl(SensorRepository sensorRepository, SensorMapper sensorMapper, SensorSearchRepository sensorSearchRepository) {
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
        this.sensorSearchRepository = sensorSearchRepository;
    }

    /**
     * Save a sensor.
     *
     * @param sensorDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorDTO save(SensorDTO sensorDTO) {
        log.debug("Request to save Sensor : {}", sensorDTO);
        Sensor sensor = sensorMapper.toEntity(sensorDTO);
        sensor = sensorRepository.save(sensor);
        SensorDTO result = sensorMapper.toDto(sensor);
        sensorSearchRepository.save(sensor);
        return result;
    }

    /**
     * Get all the sensors.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorDTO> findAll() {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll().stream()
            .map(sensorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorDTO> findOne(Long id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id)
            .map(sensorMapper::toDto);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sensor : {}", id);
        sensorRepository.deleteById(id);
        sensorSearchRepository.deleteById(id);
    }

    /**
     * Search for the sensor corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorDTO> search(String query) {
        log.debug("Request to search Sensors for query {}", query);
        return StreamSupport
            .stream(sensorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sensorMapper::toDto)
            .collect(Collectors.toList());
    }
}
