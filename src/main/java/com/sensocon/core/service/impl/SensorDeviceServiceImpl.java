package com.sensocon.core.service.impl;

import com.sensocon.core.service.SensorDeviceService;
import com.sensocon.core.domain.SensorDevice;
import com.sensocon.core.repository.SensorDeviceRepository;
import com.sensocon.core.repository.search.SensorDeviceSearchRepository;
import com.sensocon.core.service.dto.SensorDeviceDTO;
import com.sensocon.core.service.mapper.SensorDeviceMapper;
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
 * Service Implementation for managing SensorDevice.
 */
@Service
@Transactional
public class SensorDeviceServiceImpl implements SensorDeviceService {

    private final Logger log = LoggerFactory.getLogger(SensorDeviceServiceImpl.class);

    private final SensorDeviceRepository sensorDeviceRepository;

    private final SensorDeviceMapper sensorDeviceMapper;

    private final SensorDeviceSearchRepository sensorDeviceSearchRepository;

    public SensorDeviceServiceImpl(SensorDeviceRepository sensorDeviceRepository, SensorDeviceMapper sensorDeviceMapper, SensorDeviceSearchRepository sensorDeviceSearchRepository) {
        this.sensorDeviceRepository = sensorDeviceRepository;
        this.sensorDeviceMapper = sensorDeviceMapper;
        this.sensorDeviceSearchRepository = sensorDeviceSearchRepository;
    }

    /**
     * Save a sensorDevice.
     *
     * @param sensorDeviceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SensorDeviceDTO save(SensorDeviceDTO sensorDeviceDTO) {
        log.debug("Request to save SensorDevice : {}", sensorDeviceDTO);
        SensorDevice sensorDevice = sensorDeviceMapper.toEntity(sensorDeviceDTO);
        sensorDevice = sensorDeviceRepository.save(sensorDevice);
        SensorDeviceDTO result = sensorDeviceMapper.toDto(sensorDevice);
        sensorDeviceSearchRepository.save(sensorDevice);
        return result;
    }

    /**
     * Get all the sensorDevices.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorDeviceDTO> findAll() {
        log.debug("Request to get all SensorDevices");
        return sensorDeviceRepository.findAll().stream()
            .map(sensorDeviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one sensorDevice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SensorDeviceDTO> findOne(Long id) {
        log.debug("Request to get SensorDevice : {}", id);
        return sensorDeviceRepository.findById(id)
            .map(sensorDeviceMapper::toDto);
    }

    /**
     * Delete the sensorDevice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SensorDevice : {}", id);
        sensorDeviceRepository.deleteById(id);
        sensorDeviceSearchRepository.deleteById(id);
    }

    /**
     * Search for the sensorDevice corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SensorDeviceDTO> search(String query) {
        log.debug("Request to search SensorDevices for query {}", query);
        return StreamSupport
            .stream(sensorDeviceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(sensorDeviceMapper::toDto)
            .collect(Collectors.toList());
    }
}
