package com.sensocon.core.service.impl;

import com.sensocon.core.service.SensorThresholdService;
import com.sensocon.core.domain.SensorThreshold;
import com.sensocon.core.repository.SensorThresholdRepository;
import com.sensocon.core.repository.search.SensorThresholdSearchRepository;
import com.sensocon.core.service.dto.SensorThresholdDTO;
import com.sensocon.core.service.mapper.SensorThresholdMapper;
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
 * Service Implementation for managing SensorThreshold.
 */
@Service
@Transactional
public class SensorThresholdServiceImpl implements SensorThresholdService {

    private final Logger log = LoggerFactory.getLogger(SensorThresholdServiceImpl.class);

    private final SensorThresholdRepository sensorThresholdRepository;

    private final SensorThresholdMapper sensorThresholdMapper;

    private final SensorThresholdSearchRepository sensorThresholdSearchRepository;

    public SensorThresholdServiceImpl(SensorThresholdRepository sensorThresholdRepository, SensorThresholdMapper sensorThresholdMapper, SensorThresholdSearchRepository sensorThresholdSearchRepository) {
        this.sensorThresholdRepository = sensorThresholdRepository;
        this.sensorThresholdMapper = sensorThresholdMapper;
        this.sensorThresholdSearchRepository = sensorThresholdSearchRepository;
    }

    /**
     * Save a sensorThreshold.
     *
     * @param sensorThresholdDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorThresholdDTO save(SensorThresholdDTO sensorThresholdDTO) {
        log.debug("Request to save SensorThreshold : {}", sensorThresholdDTO);
        SensorThreshold sensorThreshold = sensorThresholdMapper.toEntity(sensorThresholdDTO);
        sensorThreshold = sensorThresholdRepository.save(sensorThreshold);
        SensorThresholdDTO result = sensorThresholdMapper.toDto(sensorThreshold);
        sensorThresholdSearchRepository.save(sensorThreshold);
        return result;
    }

    /**
     * Get all the sensorThresholds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorThresholdDTO> findAll() {
        log.debug("Request to get all SensorThresholds");
        return sensorThresholdRepository.findAll().stream()
            .map(sensorThresholdMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorThreshold by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorThresholdDTO> findOne(Long id) {
        log.debug("Request to get SensorThreshold : {}", id);
        return sensorThresholdRepository.findById(id)
            .map(sensorThresholdMapper::toDto);
    }

    /**
     * Delete the sensorThreshold by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorThreshold : {}", id);
        sensorThresholdRepository.deleteById(id);
        sensorThresholdSearchRepository.deleteById(id);
    }

    /**
     * Search for the sensorThreshold corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorThresholdDTO> search(String query) {
        log.debug("Request to search SensorThresholds for query {}", query);
        return StreamSupport
            .stream(sensorThresholdSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sensorThresholdMapper::toDto)
            .collect(Collectors.toList());
    }
}
