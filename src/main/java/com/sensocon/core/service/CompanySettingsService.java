package com.sensocon.core.service;

import com.sensocon.core.service.dto.CompanySettingsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CompanySettings.
 */
public interface CompanySettingsService {

    /**
     * Save a companySettings.
     *
     * @param companySettingsDTO the entity to save
     * @return the persisted entity
     */
    CompanySettingsDTO save(CompanySettingsDTO companySettingsDTO);

    /**
     * Get all the companySettings.
     *
     * @return the list of entities
     */
    List<CompanySettingsDTO> findAll();


    /**
     * Get the "id" companySettings.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CompanySettingsDTO> findOne(Long id);

    /**
     * Delete the "id" companySettings.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the companySettings corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CompanySettingsDTO> search(String query);
}
