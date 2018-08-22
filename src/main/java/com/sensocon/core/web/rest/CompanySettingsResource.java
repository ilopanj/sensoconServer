package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.CompanySettingsService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.CompanySettingsDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CompanySettings.
 */
@RestController
@RequestMapping("/api")
public class CompanySettingsResource {

    private final Logger log = LoggerFactory.getLogger(CompanySettingsResource.class);

    private static final String ENTITY_NAME = "companySettings";

    private final CompanySettingsService companySettingsService;

    public CompanySettingsResource(CompanySettingsService companySettingsService) {
        this.companySettingsService = companySettingsService;
    }

    /**
     * POST  /company-settings : Create a new companySettings.
     *
     * @param companySettingsDTO the companySettingsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companySettingsDTO, or with status 400 (Bad Request) if the companySettings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-settings")
    @Timed
    public ResponseEntity<CompanySettingsDTO> createCompanySettings(@RequestBody CompanySettingsDTO companySettingsDTO) throws URISyntaxException {
        log.debug("REST request to save CompanySettings : {}", companySettingsDTO);
        if (companySettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new companySettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanySettingsDTO result = companySettingsService.save(companySettingsDTO);
        return ResponseEntity.created(new URI("/api/company-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-settings : Updates an existing companySettings.
     *
     * @param companySettingsDTO the companySettingsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companySettingsDTO,
     * or with status 400 (Bad Request) if the companySettingsDTO is not valid,
     * or with status 500 (Internal Server Error) if the companySettingsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-settings")
    @Timed
    public ResponseEntity<CompanySettingsDTO> updateCompanySettings(@RequestBody CompanySettingsDTO companySettingsDTO) throws URISyntaxException {
        log.debug("REST request to update CompanySettings : {}", companySettingsDTO);
        if (companySettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanySettingsDTO result = companySettingsService.save(companySettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companySettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-settings : get all the companySettings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companySettings in body
     */
    @GetMapping("/company-settings")
    @Timed
    public List<CompanySettingsDTO> getAllCompanySettings() {
        log.debug("REST request to get all CompanySettings");
        return companySettingsService.findAll();
    }

    /**
     * GET  /company-settings/:id : get the "id" companySettings.
     *
     * @param id the id of the companySettingsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companySettingsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/company-settings/{id}")
    @Timed
    public ResponseEntity<CompanySettingsDTO> getCompanySettings(@PathVariable Long id) {
        log.debug("REST request to get CompanySettings : {}", id);
        Optional<CompanySettingsDTO> companySettingsDTO = companySettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companySettingsDTO);
    }

    /**
     * DELETE  /company-settings/:id : delete the "id" companySettings.
     *
     * @param id the id of the companySettingsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-settings/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanySettings(@PathVariable Long id) {
        log.debug("REST request to delete CompanySettings : {}", id);
        companySettingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-settings?query=:query : search for the companySettings corresponding
     * to the query.
     *
     * @param query the query of the companySettings search
     * @return the result of the search
     */
    @GetMapping("/_search/company-settings")
    @Timed
    public List<CompanySettingsDTO> searchCompanySettings(@RequestParam String query) {
        log.debug("REST request to search CompanySettings for query {}", query);
        return companySettingsService.search(query);
    }

}
