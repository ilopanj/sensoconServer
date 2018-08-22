package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.SensorGroupService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.SensorGroupDTO;
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
 * REST controller for managing SensorGroup.
 */
@RestController
@RequestMapping("/api")
public class SensorGroupResource {

    private final Logger log = LoggerFactory.getLogger(SensorGroupResource.class);

    private static final String ENTITY_NAME = "sensorGroup";

    private final SensorGroupService sensorGroupService;

    public SensorGroupResource(SensorGroupService sensorGroupService) {
        this.sensorGroupService = sensorGroupService;
    }

    /**
     * POST  /sensor-groups : Create a new sensorGroup.
     *
     * @param sensorGroupDTO the sensorGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensorGroupDTO, or with status 400 (Bad Request) if the sensorGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensor-groups")
    @Timed
    public ResponseEntity<SensorGroupDTO> createSensorGroup(@RequestBody SensorGroupDTO sensorGroupDTO) throws URISyntaxException {
        log.debug("REST request to save SensorGroup : {}", sensorGroupDTO);
        if (sensorGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensorGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorGroupDTO result = sensorGroupService.save(sensorGroupDTO);
        return ResponseEntity.created(new URI("/api/sensor-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensor-groups : Updates an existing sensorGroup.
     *
     * @param sensorGroupDTO the sensorGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensorGroupDTO,
     * or with status 400 (Bad Request) if the sensorGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the sensorGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensor-groups")
    @Timed
    public ResponseEntity<SensorGroupDTO> updateSensorGroup(@RequestBody SensorGroupDTO sensorGroupDTO) throws URISyntaxException {
        log.debug("REST request to update SensorGroup : {}", sensorGroupDTO);
        if (sensorGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SensorGroupDTO result = sensorGroupService.save(sensorGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensorGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensor-groups : get all the sensorGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sensorGroups in body
     */
    @GetMapping("/sensor-groups")
    @Timed
    public List<SensorGroupDTO> getAllSensorGroups() {
        log.debug("REST request to get all SensorGroups");
        return sensorGroupService.findAll();
    }

    /**
     * GET  /sensor-groups/:id : get the "id" sensorGroup.
     *
     * @param id the id of the sensorGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensorGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sensor-groups/{id}")
    @Timed
    public ResponseEntity<SensorGroupDTO> getSensorGroup(@PathVariable Long id) {
        log.debug("REST request to get SensorGroup : {}", id);
        Optional<SensorGroupDTO> sensorGroupDTO = sensorGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorGroupDTO);
    }

    /**
     * DELETE  /sensor-groups/:id : delete the "id" sensorGroup.
     *
     * @param id the id of the sensorGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensor-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteSensorGroup(@PathVariable Long id) {
        log.debug("REST request to delete SensorGroup : {}", id);
        sensorGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sensor-groups?query=:query : search for the sensorGroup corresponding
     * to the query.
     *
     * @param query the query of the sensorGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/sensor-groups")
    @Timed
    public List<SensorGroupDTO> searchSensorGroups(@RequestParam String query) {
        log.debug("REST request to search SensorGroups for query {}", query);
        return sensorGroupService.search(query);
    }

}
