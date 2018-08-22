package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.SensorDeviceService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.SensorDeviceDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SensorDevice.
 */
@RestController
@RequestMapping("/api")
public class SensorDeviceResource {

    private final Logger log = LoggerFactory.getLogger(SensorDeviceResource.class);

    private static final String ENTITY_NAME = "sensorDevice";

    private final SensorDeviceService sensorDeviceService;

    public SensorDeviceResource(SensorDeviceService sensorDeviceService) {
        this.sensorDeviceService = sensorDeviceService;
    }

    /**
     * POST  /sensor-devices : Create a new sensorDevice.
     *
     * @param sensorDeviceDTO the sensorDeviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sensorDeviceDTO, or with status 400 (Bad Request) if the sensorDevice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sensor-devices")
    @Timed
    public ResponseEntity<SensorDeviceDTO> createSensorDevice(@Valid @RequestBody SensorDeviceDTO sensorDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save SensorDevice : {}", sensorDeviceDTO);
        if (sensorDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensorDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensorDeviceDTO result = sensorDeviceService.save(sensorDeviceDTO);
        return ResponseEntity.created(new URI("/api/sensor-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sensor-devices : Updates an existing sensorDevice.
     *
     * @param sensorDeviceDTO the sensorDeviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sensorDeviceDTO,
     * or with status 400 (Bad Request) if the sensorDeviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the sensorDeviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sensor-devices")
    @Timed
    public ResponseEntity<SensorDeviceDTO> updateSensorDevice(@Valid @RequestBody SensorDeviceDTO sensorDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update SensorDevice : {}", sensorDeviceDTO);
        if (sensorDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SensorDeviceDTO result = sensorDeviceService.save(sensorDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sensorDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sensor-devices : get all the sensorDevices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sensorDevices in body
     */
    @GetMapping("/sensor-devices")
    @Timed
    public List<SensorDeviceDTO> getAllSensorDevices() {
        log.debug("REST request to get all SensorDevices");
        return sensorDeviceService.findAll();
    }

    /**
     * GET  /sensor-devices/:id : get the "id" sensorDevice.
     *
     * @param id the id of the sensorDeviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sensorDeviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sensor-devices/{id}")
    @Timed
    public ResponseEntity<SensorDeviceDTO> getSensorDevice(@PathVariable Long id) {
        log.debug("REST request to get SensorDevice : {}", id);
        Optional<SensorDeviceDTO> sensorDeviceDTO = sensorDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sensorDeviceDTO);
    }

    /**
     * DELETE  /sensor-devices/:id : delete the "id" sensorDevice.
     *
     * @param id the id of the sensorDeviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sensor-devices/{id}")
    @Timed
    public ResponseEntity<Void> deleteSensorDevice(@PathVariable Long id) {
        log.debug("REST request to delete SensorDevice : {}", id);
        sensorDeviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sensor-devices?query=:query : search for the sensorDevice corresponding
     * to the query.
     *
     * @param query the query of the sensorDevice search
     * @return the result of the search
     */
    @GetMapping("/_search/sensor-devices")
    @Timed
    public List<SensorDeviceDTO> searchSensorDevices(@RequestParam String query) {
        log.debug("REST request to search SensorDevices for query {}", query);
        return sensorDeviceService.search(query);
    }

}
