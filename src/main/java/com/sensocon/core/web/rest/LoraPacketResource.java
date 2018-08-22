package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.LoraPacketService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.LoraPacketDTO;
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
 * REST controller for managing LoraPacket.
 */
@RestController
@RequestMapping("/api")
public class LoraPacketResource {

    private final Logger log = LoggerFactory.getLogger(LoraPacketResource.class);

    private static final String ENTITY_NAME = "loraPacket";

    private final LoraPacketService loraPacketService;

    public LoraPacketResource(LoraPacketService loraPacketService) {
        this.loraPacketService = loraPacketService;
    }

    /**
     * POST  /lora-packets : Create a new loraPacket.
     *
     * @param loraPacketDTO the loraPacketDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loraPacketDTO, or with status 400 (Bad Request) if the loraPacket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lora-packets")
    @Timed
    public ResponseEntity<LoraPacketDTO> createLoraPacket(@RequestBody LoraPacketDTO loraPacketDTO) throws URISyntaxException {
        log.debug("REST request to save LoraPacket : {}", loraPacketDTO);
        if (loraPacketDTO.getId() != null) {
            throw new BadRequestAlertException("A new loraPacket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoraPacketDTO result = loraPacketService.save(loraPacketDTO);
        return ResponseEntity.created(new URI("/api/lora-packets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lora-packets : Updates an existing loraPacket.
     *
     * @param loraPacketDTO the loraPacketDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loraPacketDTO,
     * or with status 400 (Bad Request) if the loraPacketDTO is not valid,
     * or with status 500 (Internal Server Error) if the loraPacketDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lora-packets")
    @Timed
    public ResponseEntity<LoraPacketDTO> updateLoraPacket(@RequestBody LoraPacketDTO loraPacketDTO) throws URISyntaxException {
        log.debug("REST request to update LoraPacket : {}", loraPacketDTO);
        if (loraPacketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoraPacketDTO result = loraPacketService.save(loraPacketDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loraPacketDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lora-packets : get all the loraPackets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loraPackets in body
     */
    @GetMapping("/lora-packets")
    @Timed
    public List<LoraPacketDTO> getAllLoraPackets() {
        log.debug("REST request to get all LoraPackets");
        return loraPacketService.findAll();
    }

    /**
     * GET  /lora-packets/:id : get the "id" loraPacket.
     *
     * @param id the id of the loraPacketDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loraPacketDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lora-packets/{id}")
    @Timed
    public ResponseEntity<LoraPacketDTO> getLoraPacket(@PathVariable Long id) {
        log.debug("REST request to get LoraPacket : {}", id);
        Optional<LoraPacketDTO> loraPacketDTO = loraPacketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loraPacketDTO);
    }

    /**
     * DELETE  /lora-packets/:id : delete the "id" loraPacket.
     *
     * @param id the id of the loraPacketDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lora-packets/{id}")
    @Timed
    public ResponseEntity<Void> deleteLoraPacket(@PathVariable Long id) {
        log.debug("REST request to delete LoraPacket : {}", id);
        loraPacketService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lora-packets?query=:query : search for the loraPacket corresponding
     * to the query.
     *
     * @param query the query of the loraPacket search
     * @return the result of the search
     */
    @GetMapping("/_search/lora-packets")
    @Timed
    public List<LoraPacketDTO> searchLoraPackets(@RequestParam String query) {
        log.debug("REST request to search LoraPackets for query {}", query);
        return loraPacketService.search(query);
    }

}
