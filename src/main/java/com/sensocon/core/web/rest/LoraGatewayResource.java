package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.LoraGatewayService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.LoraGatewayDTO;
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
 * REST controller for managing LoraGateway.
 */
@RestController
@RequestMapping("/api")
public class LoraGatewayResource {

    private final Logger log = LoggerFactory.getLogger(LoraGatewayResource.class);

    private static final String ENTITY_NAME = "loraGateway";

    private final LoraGatewayService loraGatewayService;

    public LoraGatewayResource(LoraGatewayService loraGatewayService) {
        this.loraGatewayService = loraGatewayService;
    }

    /**
     * POST  /lora-gateways : Create a new loraGateway.
     *
     * @param loraGatewayDTO the loraGatewayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loraGatewayDTO, or with status 400 (Bad Request) if the loraGateway has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lora-gateways")
    @Timed
    public ResponseEntity<LoraGatewayDTO> createLoraGateway(@Valid @RequestBody LoraGatewayDTO loraGatewayDTO) throws URISyntaxException {
        log.debug("REST request to save LoraGateway : {}", loraGatewayDTO);
        if (loraGatewayDTO.getId() != null) {
            throw new BadRequestAlertException("A new loraGateway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoraGatewayDTO result = loraGatewayService.save(loraGatewayDTO);
        return ResponseEntity.created(new URI("/api/lora-gateways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lora-gateways : Updates an existing loraGateway.
     *
     * @param loraGatewayDTO the loraGatewayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loraGatewayDTO,
     * or with status 400 (Bad Request) if the loraGatewayDTO is not valid,
     * or with status 500 (Internal Server Error) if the loraGatewayDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lora-gateways")
    @Timed
    public ResponseEntity<LoraGatewayDTO> updateLoraGateway(@Valid @RequestBody LoraGatewayDTO loraGatewayDTO) throws URISyntaxException {
        log.debug("REST request to update LoraGateway : {}", loraGatewayDTO);
        if (loraGatewayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoraGatewayDTO result = loraGatewayService.save(loraGatewayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loraGatewayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lora-gateways : get all the loraGateways.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loraGateways in body
     */
    @GetMapping("/lora-gateways")
    @Timed
    public List<LoraGatewayDTO> getAllLoraGateways() {
        log.debug("REST request to get all LoraGateways");
        return loraGatewayService.findAll();
    }

    /**
     * GET  /lora-gateways/:id : get the "id" loraGateway.
     *
     * @param id the id of the loraGatewayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loraGatewayDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lora-gateways/{id}")
    @Timed
    public ResponseEntity<LoraGatewayDTO> getLoraGateway(@PathVariable Long id) {
        log.debug("REST request to get LoraGateway : {}", id);
        Optional<LoraGatewayDTO> loraGatewayDTO = loraGatewayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loraGatewayDTO);
    }

    /**
     * DELETE  /lora-gateways/:id : delete the "id" loraGateway.
     *
     * @param id the id of the loraGatewayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lora-gateways/{id}")
    @Timed
    public ResponseEntity<Void> deleteLoraGateway(@PathVariable Long id) {
        log.debug("REST request to delete LoraGateway : {}", id);
        loraGatewayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lora-gateways?query=:query : search for the loraGateway corresponding
     * to the query.
     *
     * @param query the query of the loraGateway search
     * @return the result of the search
     */
    @GetMapping("/_search/lora-gateways")
    @Timed
    public List<LoraGatewayDTO> searchLoraGateways(@RequestParam String query) {
        log.debug("REST request to search LoraGateways for query {}", query);
        return loraGatewayService.search(query);
    }

}
