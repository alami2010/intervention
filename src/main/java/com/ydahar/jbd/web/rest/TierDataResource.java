package com.ydahar.jbd.web.rest;

import com.ydahar.jbd.repository.TierDataRepository;
import com.ydahar.jbd.service.TierDataService;
import com.ydahar.jbd.service.dto.TierDataDTO;
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
 * REST controller for managing {@link com.ydahar.jbd.domain.TierData}.
 */
@RestController
@RequestMapping("/api")
public class TierDataResource {

    private final Logger log = LoggerFactory.getLogger(TierDataResource.class);

    private static final String ENTITY_NAME = "tierData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TierDataService tierDataService;

    private final TierDataRepository tierDataRepository;

    public TierDataResource(TierDataService tierDataService, TierDataRepository tierDataRepository) {
        this.tierDataService = tierDataService;
        this.tierDataRepository = tierDataRepository;
    }

    /**
     * {@code POST  /tier-data} : Create a new tierData.
     *
     * @param tierDataDTO the tierDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tierDataDTO, or with status {@code 400 (Bad Request)} if the tierData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tier-data")
    public ResponseEntity<TierDataDTO> createTierData(@RequestBody TierDataDTO tierDataDTO) throws URISyntaxException {
        log.debug("REST request to save TierData : {}", tierDataDTO);
        if (tierDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new tierData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TierDataDTO result = tierDataService.save(tierDataDTO);
        return ResponseEntity
            .created(new URI("/api/tier-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tier-data/:id} : Updates an existing tierData.
     *
     * @param id the id of the tierDataDTO to save.
     * @param tierDataDTO the tierDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierDataDTO,
     * or with status {@code 400 (Bad Request)} if the tierDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tierDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tier-data/{id}")
    public ResponseEntity<TierDataDTO> updateTierData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TierDataDTO tierDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TierData : {}, {}", id, tierDataDTO);
        if (tierDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TierDataDTO result = tierDataService.update(tierDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tier-data/:id} : Partial updates given fields of an existing tierData, field will ignore if it is null
     *
     * @param id the id of the tierDataDTO to save.
     * @param tierDataDTO the tierDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierDataDTO,
     * or with status {@code 400 (Bad Request)} if the tierDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tierDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tierDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tier-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TierDataDTO> partialUpdateTierData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TierDataDTO tierDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TierData partially : {}, {}", id, tierDataDTO);
        if (tierDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TierDataDTO> result = tierDataService.partialUpdate(tierDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tier-data} : get all the tierData.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tierData in body.
     */
    @GetMapping("/tier-data")
    public ResponseEntity<List<TierDataDTO>> getAllTierData(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TierData");
        Page<TierDataDTO> page = tierDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tier-data/:id} : get the "id" tierData.
     *
     * @param id the id of the tierDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tierDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tier-data/{id}")
    public ResponseEntity<TierDataDTO> getTierData(@PathVariable Long id) {
        log.debug("REST request to get TierData : {}", id);
        Optional<TierDataDTO> tierDataDTO = tierDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tierDataDTO);
    }

    /**
     * {@code DELETE  /tier-data/:id} : delete the "id" tierData.
     *
     * @param id the id of the tierDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tier-data/{id}")
    public ResponseEntity<Void> deleteTierData(@PathVariable Long id) {
        log.debug("REST request to delete TierData : {}", id);
        tierDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
