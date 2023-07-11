package com.ydahar.jbd.web.rest;

import com.ydahar.jbd.repository.TierRepository;
import com.ydahar.jbd.service.TierService;
import com.ydahar.jbd.service.dto.TierDTO;
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
 * REST controller for managing {@link com.ydahar.jbd.domain.Tier}.
 */
@RestController
@RequestMapping("/api")
public class TierResource {

    private final Logger log = LoggerFactory.getLogger(TierResource.class);

    private static final String ENTITY_NAME = "tier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TierService tierService;

    private final TierRepository tierRepository;

    public TierResource(TierService tierService, TierRepository tierRepository) {
        this.tierService = tierService;
        this.tierRepository = tierRepository;
    }

    /**
     * {@code POST  /tiers} : Create a new tier.
     *
     * @param tierDTO the tierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tierDTO, or with status {@code 400 (Bad Request)} if the tier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tiers")
    public ResponseEntity<TierDTO> createTier(@RequestBody TierDTO tierDTO) throws URISyntaxException {
        log.debug("REST request to save Tier : {}", tierDTO);
        if (tierDTO.getId() != null) {
            throw new BadRequestAlertException("A new tier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TierDTO result = tierService.save(tierDTO);
        return ResponseEntity
            .created(new URI("/api/tiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tiers/:id} : Updates an existing tier.
     *
     * @param id the id of the tierDTO to save.
     * @param tierDTO the tierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierDTO,
     * or with status {@code 400 (Bad Request)} if the tierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tiers/{id}")
    public ResponseEntity<TierDTO> updateTier(@PathVariable(value = "id", required = false) final Long id, @RequestBody TierDTO tierDTO)
        throws URISyntaxException {
        log.debug("REST request to update Tier : {}, {}", id, tierDTO);
        if (tierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TierDTO result = tierService.update(tierDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tiers/:id} : Partial updates given fields of an existing tier, field will ignore if it is null
     *
     * @param id the id of the tierDTO to save.
     * @param tierDTO the tierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tierDTO,
     * or with status {@code 400 (Bad Request)} if the tierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TierDTO> partialUpdateTier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TierDTO tierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tier partially : {}, {}", id, tierDTO);
        if (tierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TierDTO> result = tierService.partialUpdate(tierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tiers} : get all the tiers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tiers in body.
     */
    @GetMapping("/tiers")
    public ResponseEntity<List<TierDTO>> getAllTiers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tiers");
        Page<TierDTO> page = tierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tiers/:id} : get the "id" tier.
     *
     * @param id the id of the tierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tiers/{id}")
    public ResponseEntity<TierDTO> getTier(@PathVariable Long id) {
        log.debug("REST request to get Tier : {}", id);
        Optional<TierDTO> tierDTO = tierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tierDTO);
    }

    /**
     * {@code DELETE  /tiers/:id} : delete the "id" tier.
     *
     * @param id the id of the tierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tiers/{id}")
    public ResponseEntity<Void> deleteTier(@PathVariable Long id) {
        log.debug("REST request to delete Tier : {}", id);
        tierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
