package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.SensorThresholdsService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.SensorThresholdsDTO;
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
 * REST controller for managing SensorThresholds.
 */
@RestController
@RequestMapping("/api")
public class SensorThresholdsResource {

    private final Logger log = LoggerFactory.getLogger(SensorThresholdsResource.class);

    private static final String ENTITY_NAME = "sensorThresholds";

    private final SensorThresholdsService sensorThresholdsService;

    public SensorThresholdsResource(SensorThresholdsService sensorThresholdsService) {
        this.sensorThresholdsService = sensorThresholdsService;
    }

    /**
     * POST  /sensor-thresholds : Create a new sensorThresholds.
     *
     * @param sensorThresholdsDTO the sensorThresholdsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensorThresholdsDTO, or with status 400 (Bad Request) if the sensorThresholds has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensor-thresholds")
    @Timed
    public ResponseEntity<SensorThresholdsDTO> createSensorThresholds(@RequestBody SensorThresholdsDTO sensorThresholdsDTO) throws URISyntaxException {
        log.debug("REST request to save SensorThresholds : {}", sensorThresholdsDTO);
        if (sensorThresholdsDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensorThresholds cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorThresholdsDTO result = sensorThresholdsService.save(sensorThresholdsDTO);
        return ResponseEntity.created(new URI("/api/sensor-thresholds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensor-thresholds : Updates an existing sensorThresholds.
     *
     * @param sensorThresholdsDTO the sensorThresholdsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensorThresholdsDTO,
     * or with status 400 (Bad Request) if the sensorThresholdsDTO is not valid,
     * or with status 500 (Internal Server Error) if the sensorThresholdsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensor-thresholds")
    @Timed
    public ResponseEntity<SensorThresholdsDTO> updateSensorThresholds(@RequestBody SensorThresholdsDTO sensorThresholdsDTO) throws URISyntaxException {
        log.debug("REST request to update SensorThresholds : {}", sensorThresholdsDTO);
        if (sensorThresholdsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SensorThresholdsDTO result = sensorThresholdsService.save(sensorThresholdsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensorThresholdsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensor-thresholds : get all the sensorThresholds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sensorThresholds in body
     */
    @GetMapping("/sensor-thresholds")
    @Timed
    public List<SensorThresholdsDTO> getAllSensorThresholds() {
        log.debug("REST request to get all SensorThresholds");
        return sensorThresholdsService.findAll();
    }

    /**
     * GET  /sensor-thresholds/:id : get the "id" sensorThresholds.
     *
     * @param id the id of the sensorThresholdsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensorThresholdsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sensor-thresholds/{id}")
    @Timed
    public ResponseEntity<SensorThresholdsDTO> getSensorThresholds(@PathVariable Long id) {
        log.debug("REST request to get SensorThresholds : {}", id);
        Optional<SensorThresholdsDTO> sensorThresholdsDTO = sensorThresholdsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorThresholdsDTO);
    }

    /**
     * DELETE  /sensor-thresholds/:id : delete the "id" sensorThresholds.
     *
     * @param id the id of the sensorThresholdsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensor-thresholds/{id}")
    @Timed
    public ResponseEntity<Void> deleteSensorThresholds(@PathVariable Long id) {
        log.debug("REST request to delete SensorThresholds : {}", id);
        sensorThresholdsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sensor-thresholds?query=:query : search for the sensorThresholds corresponding
     * to the query.
     *
     * @param query the query of the sensorThresholds search
     * @return the result of the search
     */
    @GetMapping("/_search/sensor-thresholds")
    @Timed
    public List<SensorThresholdsDTO> searchSensorThresholds(@RequestParam String query) {
        log.debug("REST request to search SensorThresholds for query {}", query);
        return sensorThresholdsService.search(query);
    }

}
