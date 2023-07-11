package com.ydahar.jbd.web.rest;

import com.ydahar.jbd.repository.RoomDataRepository;
import com.ydahar.jbd.service.RoomDataService;
import com.ydahar.jbd.service.dto.RoomDataDTO;
import com.ydahar.jbd.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ydahar.jbd.domain.RoomData}.
 */
@RestController
@RequestMapping("/api")
public class RoomDataResource {

    private final Logger log = LoggerFactory.getLogger(RoomDataResource.class);

    private static final String ENTITY_NAME = "roomData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomDataService roomDataService;

    private final RoomDataRepository roomDataRepository;

    public RoomDataResource(RoomDataService roomDataService, RoomDataRepository roomDataRepository) {
        this.roomDataService = roomDataService;
        this.roomDataRepository = roomDataRepository;
    }

    /**
     * {@code POST  /room-data} : Create a new roomData.
     *
     * @param roomDataDTO the roomDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomDataDTO, or with status {@code 400 (Bad Request)} if the roomData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-data")
    public ResponseEntity<RoomDataDTO> createRoomData(@RequestBody RoomDataDTO roomDataDTO) throws URISyntaxException {
        log.debug("REST request to save RoomData : {}", roomDataDTO);
        if (roomDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomDataDTO result = roomDataService.save(roomDataDTO);
        return ResponseEntity
            .created(new URI("/api/room-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-data/:id} : Updates an existing roomData.
     *
     * @param id the id of the roomDataDTO to save.
     * @param roomDataDTO the roomDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomDataDTO,
     * or with status {@code 400 (Bad Request)} if the roomDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-data/{id}")
    public ResponseEntity<RoomDataDTO> updateRoomData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomDataDTO roomDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoomData : {}, {}", id, roomDataDTO);
        if (roomDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomDataDTO result = roomDataService.update(roomDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roomDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-data/:id} : Partial updates given fields of an existing roomData, field will ignore if it is null
     *
     * @param id the id of the roomDataDTO to save.
     * @param roomDataDTO the roomDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomDataDTO,
     * or with status {@code 400 (Bad Request)} if the roomDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roomDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomDataDTO> partialUpdateRoomData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomDataDTO roomDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomData partially : {}, {}", id, roomDataDTO);
        if (roomDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomDataDTO> result = roomDataService.partialUpdate(roomDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roomDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /room-data} : get all the roomData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomData in body.
     */
    @GetMapping("/room-data")
    public ResponseEntity<List<RoomDataDTO>> getAllRoomData(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RoomData");
        Page<RoomDataDTO> page = roomDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-data/:id} : get the "id" roomData.
     *
     * @param id the id of the roomDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-data/{id}")
    public ResponseEntity<RoomDataDTO> getRoomData(@PathVariable Long id) {
        log.debug("REST request to get RoomData : {}", id);
        Optional<RoomDataDTO> roomDataDTO = roomDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomDataDTO);
    }

    /**
     * {@code DELETE  /room-data/:id} : delete the "id" roomData.
     *
     * @param id the id of the roomDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-data/{id}")
    public ResponseEntity<Void> deleteRoomData(@PathVariable Long id) {
        log.debug("REST request to delete RoomData : {}", id);
        roomDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
