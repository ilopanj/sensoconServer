package com.sensocon.core.service.impl;

import com.sensocon.core.service.LoraGatewayService;
import com.sensocon.core.domain.LoraGateway;
import com.sensocon.core.repository.LoraGatewayRepository;
import com.sensocon.core.repository.search.LoraGatewaySearchRepository;
import com.sensocon.core.service.dto.LoraGatewayDTO;
import com.sensocon.core.service.mapper.LoraGatewayMapper;
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
 * Service Implementation for managing LoraGateway.
 */
@Service
@Transactional
public class LoraGatewayServiceImpl implements LoraGatewayService {

    private final Logger log = LoggerFactory.getLogger(LoraGatewayServiceImpl.class);

    private final LoraGatewayRepository loraGatewayRepository;

    private final LoraGatewayMapper loraGatewayMapper;

    private final LoraGatewaySearchRepository loraGatewaySearchRepository;

    public LoraGatewayServiceImpl(LoraGatewayRepository loraGatewayRepository, LoraGatewayMapper loraGatewayMapper, LoraGatewaySearchRepository loraGatewaySearchRepository) {
        this.loraGatewayRepository = loraGatewayRepository;
        this.loraGatewayMapper = loraGatewayMapper;
        this.loraGatewaySearchRepository = loraGatewaySearchRepository;
    }

    /**
     * Save a loraGateway.
     *
     * @param loraGatewayDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LoraGatewayDTO save(LoraGatewayDTO loraGatewayDTO) {
        log.debug("Request to save LoraGateway : {}", loraGatewayDTO);
        LoraGateway loraGateway = loraGatewayMapper.toEntity(loraGatewayDTO);
        loraGateway = loraGatewayRepository.save(loraGateway);
        LoraGatewayDTO result = loraGatewayMapper.toDto(loraGateway);
        loraGatewaySearchRepository.save(loraGateway);
        return result;
    }

    /**
     * Get all the loraGateways.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoraGatewayDTO> findAll() {
        log.debug("Request to get all LoraGateways");
        return loraGatewayRepository.findAll().stream()
            .map(loraGatewayMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one loraGateway by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LoraGatewayDTO> findOne(Long id) {
        log.debug("Request to get LoraGateway : {}", id);
        return loraGatewayRepository.findById(id)
            .map(loraGatewayMapper::toDto);
    }

    /**
     * Delete the loraGateway by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoraGateway : {}", id);
        loraGatewayRepository.deleteById(id);
        loraGatewaySearchRepository.deleteById(id);
    }

    /**
     * Search for the loraGateway corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoraGatewayDTO> search(String query) {
        log.debug("Request to search LoraGateways for query {}", query);
        return StreamSupport
            .stream(loraGatewaySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(loraGatewayMapper::toDto)
            .collect(Collectors.toList());
    }
}
