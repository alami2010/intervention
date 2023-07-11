package com.ydahar.jbd.web.rest;

import com.ydahar.jbd.repository.FloorDataRepository;
import com.ydahar.jbd.service.FloorDataService;
import com.ydahar.jbd.service.dto.FloorDataDTO;
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
 * REST controller for managing {@link com.ydahar.jbd.domain.FloorData}.
 */
@RestController
@RequestMapping("/api")
public class FloorDataResource {

    private final Logger log = LoggerFactory.getLogger(FloorDataResource.class);

    private static final String ENTITY_NAME = "floorData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FloorDataService floorDataService;

    private final FloorDataRepository floorDataRepository;

    public FloorDataResource(FloorDataService floorDataService, FloorDataRepository floorDataRepository) {
        this.floorDataService = floorDataService;
        this.floorDataRepository = floorDataRepository;
    }

    /**
     * {@code POST  /floor-data} : Create a new floorData.
     *
     * @param floorDataDTO the floorDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floorDataDTO, or with status {@code 400 (Bad Request)} if the floorData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/floor-data")
    public ResponseEntity<FloorDataDTO> createFloorData(@RequestBody FloorDataDTO floorDataDTO) throws URISyntaxException {
        log.debug("REST request to save FloorData : {}", floorDataDTO);
        if (floorDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new floorData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FloorDataDTO result = floorDataService.save(floorDataDTO);
        return ResponseEntity
            .created(new URI("/api/floor-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /floor-data/:id} : Updates an existing floorData.
     *
     * @param id the id of the floorDataDTO to save.
     * @param floorDataDTO the floorDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floorDataDTO,
     * or with status {@code 400 (Bad Request)} if the floorDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floorDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/floor-data/{id}")
    public ResponseEntity<FloorDataDTO> updateFloorData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FloorDataDTO floorDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FloorData : {}, {}", id, floorDataDTO);
        if (floorDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floorDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FloorDataDTO result = floorDataService.update(floorDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floorDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /floor-data/:id} : Partial updates given fields of an existing floorData, field will ignore if it is null
     *
     * @param id the id of the floorDataDTO to save.
     * @param floorDataDTO the floorDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floorDataDTO,
     * or with status {@code 400 (Bad Request)} if the floorDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the floorDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the floorDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/floor-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FloorDataDTO> partialUpdateFloorData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FloorDataDTO floorDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FloorData partially : {}, {}", id, floorDataDTO);
        if (floorDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floorDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floorDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FloorDataDTO> result = floorDataService.partialUpdate(floorDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, floorDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /floor-data} : get all the floorData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floorData in body.
     */
    @GetMapping("/floor-data")
    public ResponseEntity<List<FloorDataDTO>> getAllFloorData(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FloorData");
        Page<FloorDataDTO> page = floorDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /floor-data/:id} : get the "id" floorData.
     *
     * @param id the id of the floorDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floorDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/floor-data/{id}")
    public ResponseEntity<FloorDataDTO> getFloorData(@PathVariable Long id) {
        log.debug("REST request to get FloorData : {}", id);
        Optional<FloorDataDTO> floorDataDTO = floorDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(floorDataDTO);
    }

    /**
     * {@code DELETE  /floor-data/:id} : delete the "id" floorData.
     *
     * @param id the id of the floorDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/floor-data/{id}")
    public ResponseEntity<Void> deleteFloorData(@PathVariable Long id) {
        log.debug("REST request to delete FloorData : {}", id);
        floorDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
