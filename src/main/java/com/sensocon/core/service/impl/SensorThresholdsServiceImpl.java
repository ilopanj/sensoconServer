package com.sensocon.core.service.impl;

import com.sensocon.core.service.SensorThresholdsService;
import com.sensocon.core.domain.SensorThresholds;
import com.sensocon.core.repository.SensorThresholdsRepository;
import com.sensocon.core.repository.search.SensorThresholdsSearchRepository;
import com.sensocon.core.service.dto.SensorThresholdsDTO;
import com.sensocon.core.service.mapper.SensorThresholdsMapper;
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
 * Service Implementation for managing SensorThresholds.
 */
@Service
@Transactional
public class SensorThresholdsServiceImpl implements SensorThresholdsService {

    private final Logger log = LoggerFactory.getLogger(SensorThresholdsServiceImpl.class);

    private final SensorThresholdsRepository sensorThresholdsRepository;

    private final SensorThresholdsMapper sensorThresholdsMapper;

    private final SensorThresholdsSearchRepository sensorThresholdsSearchRepository;

    public SensorThresholdsServiceImpl(SensorThresholdsRepository sensorThresholdsRepository, SensorThresholdsMapper sensorThresholdsMapper, SensorThresholdsSearchRepository sensorThresholdsSearchRepository) {
        this.sensorThresholdsRepository = sensorThresholdsRepository;
        this.sensorThresholdsMapper = sensorThresholdsMapper;
        this.sensorThresholdsSearchRepository = sensorThresholdsSearchRepository;
    }

    /**
     * Save a sensorThresholds.
     *
     * @param sensorThresholdsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorThresholdsDTO save(SensorThresholdsDTO sensorThresholdsDTO) {
        log.debug("Request to save SensorThresholds : {}", sensorThresholdsDTO);
        SensorThresholds sensorThresholds = sensorThresholdsMapper.toEntity(sensorThresholdsDTO);
        sensorThresholds = sensorThresholdsRepository.save(sensorThresholds);
        SensorThresholdsDTO result = sensorThresholdsMapper.toDto(sensorThresholds);
        sensorThresholdsSearchRepository.save(sensorThresholds);
        return result;
    }

    /**
     * Get all the sensorThresholds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorThresholdsDTO> findAll() {
        log.debug("Request to get all SensorThresholds");
        return sensorThresholdsRepository.findAll().stream()
            .map(sensorThresholdsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorThresholds by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorThresholdsDTO> findOne(Long id) {
        log.debug("Request to get SensorThresholds : {}", id);
        return sensorThresholdsRepository.findById(id)
            .map(sensorThresholdsMapper::toDto);
    }

    /**
     * Delete the sensorThresholds by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorThresholds : {}", id);
        sensorThresholdsRepository.deleteById(id);
        sensorThresholdsSearchRepository.deleteById(id);
    }

    /**
     * Search for the sensorThresholds corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorThresholdsDTO> search(String query) {
        log.debug("Request to search SensorThresholds for query {}", query);
        return StreamSupport
            .stream(sensorThresholdsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sensorThresholdsMapper::toDto)
            .collect(Collectors.toList());
    }
}
