package com.sensocon.core.service;

import com.sensocon.core.service.dto.NotificationGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing NotificationGroup.
 */
public interface NotificationGroupService {

    /**
     * Save a notificationGroup.
     *
     * @param notificationGroupDTO the entity to save
     * @return the persisted entity
     */
    NotificationGroupDTO save(NotificationGroupDTO notificationGroupDTO);

    /**
     * Get all the notificationGroups.
     *
     * @return the list of entities
     */
    List<NotificationGroupDTO> findAll();


    /**
     * Get the "id" notificationGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<NotificationGroupDTO> findOne(Long id);

    /**
     * Delete the "id" notificationGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the notificationGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<NotificationGroupDTO> search(String query);
}
