package com.sensocon.core.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sensocon.core.service.NotificationGroupService;
import com.sensocon.core.web.rest.errors.BadRequestAlertException;
import com.sensocon.core.web.rest.util.HeaderUtil;
import com.sensocon.core.service.dto.NotificationGroupDTO;
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
 * REST controller for managing NotificationGroup.
 */
@RestController
@RequestMapping("/api")
public class NotificationGroupResource {

    private final Logger log = LoggerFactory.getLogger(NotificationGroupResource.class);

    private static final String ENTITY_NAME = "notificationGroup";

    private final NotificationGroupService notificationGroupService;

    public NotificationGroupResource(NotificationGroupService notificationGroupService) {
        this.notificationGroupService = notificationGroupService;
    }

    /**
     * POST  /notification-groups : Create a new notificationGroup.
     *
     * @param notificationGroupDTO the notificationGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationGroupDTO, or with status 400 (Bad Request) if the notificationGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notification-groups")
    @Timed
    public ResponseEntity<NotificationGroupDTO> createNotificationGroup(@RequestBody NotificationGroupDTO notificationGroupDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationGroup : {}", notificationGroupDTO);
        if (notificationGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationGroupDTO result = notificationGroupService.save(notificationGroupDTO);
        return ResponseEntity.created(new URI("/api/notification-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-groups : Updates an existing notificationGroup.
     *
     * @param notificationGroupDTO the notificationGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationGroupDTO,
     * or with status 400 (Bad Request) if the notificationGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notification-groups")
    @Timed
    public ResponseEntity<NotificationGroupDTO> updateNotificationGroup(@RequestBody NotificationGroupDTO notificationGroupDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationGroup : {}", notificationGroupDTO);
        if (notificationGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationGroupDTO result = notificationGroupService.save(notificationGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, notificationGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-groups : get all the notificationGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificationGroups in body
     */
    @GetMapping("/notification-groups")
    @Timed
    public List<NotificationGroupDTO> getAllNotificationGroups() {
        log.debug("REST request to get all NotificationGroups");
        return notificationGroupService.findAll();
    }

    /**
     * GET  /notification-groups/:id : get the "id" notificationGroup.
     *
     * @param id the id of the notificationGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/notification-groups/{id}")
    @Timed
    public ResponseEntity<NotificationGroupDTO> getNotificationGroup(@PathVariable Long id) {
        log.debug("REST request to get NotificationGroup : {}", id);
        Optional<NotificationGroupDTO> notificationGroupDTO = notificationGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationGroupDTO);
    }

    /**
     * DELETE  /notification-groups/:id : delete the "id" notificationGroup.
     *
     * @param id the id of the notificationGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notification-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteNotificationGroup(@PathVariable Long id) {
        log.debug("REST request to delete NotificationGroup : {}", id);
        notificationGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-groups?query=:query : search for the notificationGroup corresponding
     * to the query.
     *
     * @param query the query of the notificationGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/notification-groups")
    @Timed
    public List<NotificationGroupDTO> searchNotificationGroups(@RequestParam String query) {
        log.debug("REST request to search NotificationGroups for query {}", query);
        return notificationGroupService.search(query);
    }

}
