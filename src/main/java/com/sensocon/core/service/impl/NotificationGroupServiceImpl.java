package com.sensocon.core.service.impl;

import com.sensocon.core.service.NotificationGroupService;
import com.sensocon.core.domain.NotificationGroup;
import com.sensocon.core.repository.NotificationGroupRepository;
import com.sensocon.core.repository.search.NotificationGroupSearchRepository;
import com.sensocon.core.service.dto.NotificationGroupDTO;
import com.sensocon.core.service.mapper.NotificationGroupMapper;
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
 * Service Implementation for managing NotificationGroup.
 */
@Service
@Transactional
public class NotificationGroupServiceImpl implements NotificationGroupService {

    private final Logger log = LoggerFactory.getLogger(NotificationGroupServiceImpl.class);

    private final NotificationGroupRepository notificationGroupRepository;

    private final NotificationGroupMapper notificationGroupMapper;

    private final NotificationGroupSearchRepository notificationGroupSearchRepository;

    public NotificationGroupServiceImpl(NotificationGroupRepository notificationGroupRepository, NotificationGroupMapper notificationGroupMapper, NotificationGroupSearchRepository notificationGroupSearchRepository) {
        this.notificationGroupRepository = notificationGroupRepository;
        this.notificationGroupMapper = notificationGroupMapper;
        this.notificationGroupSearchRepository = notificationGroupSearchRepository;
    }

    /**
     * Save a notificationGroup.
     *
     * @param notificationGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NotificationGroupDTO save(NotificationGroupDTO notificationGroupDTO) {
        log.debug("Request to save NotificationGroup : {}", notificationGroupDTO);
        NotificationGroup notificationGroup = notificationGroupMapper.toEntity(notificationGroupDTO);
        notificationGroup = notificationGroupRepository.save(notificationGroup);
        NotificationGroupDTO result = notificationGroupMapper.toDto(notificationGroup);
        notificationGroupSearchRepository.save(notificationGroup);
        return result;
    }

    /**
     * Get all the notificationGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificationGroupDTO> findAll() {
        log.debug("Request to get all NotificationGroups");
        return notificationGroupRepository.findAll().stream()
            .map(notificationGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one notificationGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationGroupDTO> findOne(Long id) {
        log.debug("Request to get NotificationGroup : {}", id);
        return notificationGroupRepository.findById(id)
            .map(notificationGroupMapper::toDto);
    }

    /**
     * Delete the notificationGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NotificationGroup : {}", id);
        notificationGroupRepository.deleteById(id);
        notificationGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the notificationGroup corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificationGroupDTO> search(String query) {
        log.debug("Request to search NotificationGroups for query {}", query);
        return StreamSupport
            .stream(notificationGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(notificationGroupMapper::toDto)
            .collect(Collectors.toList());
    }
}
