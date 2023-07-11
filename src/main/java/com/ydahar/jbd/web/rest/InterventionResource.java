package com.ydahar.jbd.web.rest;

import com.lowagie.text.DocumentException;
import com.ydahar.jbd.repository.InterventionRepository;
import com.ydahar.jbd.service.InterventionService;
import com.ydahar.jbd.service.dto.InterventionDTO;
import com.ydahar.jbd.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ydahar.jbd.domain.Intervention}.
 */
@RestController
@RequestMapping("/api")
public class InterventionResource {

    private final Logger log = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionService interventionService;

    private final InterventionRepository interventionRepository;

    public InterventionResource(InterventionService interventionService, InterventionRepository interventionRepository) {
        this.interventionService = interventionService;
        this.interventionRepository = interventionRepository;
    }

    /**
     * {@code POST  /interventions} : Create a new intervention.
     *
     * @param interventionDTO the interventionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interventionDTO, or with status {@code 400 (Bad Request)} if the intervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interventions")
    public ResponseEntity<InterventionDTO> createIntervention(@RequestBody InterventionDTO interventionDTO)
        throws URISyntaxException, DocumentException, IOException {
        log.debug("REST request to save Intervention : {}", interventionDTO);
        if (interventionDTO.getId() == -1) {
            interventionDTO.setId(null);
        }
        if (interventionDTO.getId() != null) {
            throw new BadRequestAlertException("A new intervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterventionDTO result = interventionService.save(interventionDTO);
        return ResponseEntity
            .created(new URI("/api/interventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interventions/:id} : Updates an existing intervention.
     *
     * @param id              the id of the interventionDTO to save.
     * @param interventionDTO the interventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionDTO,
     * or with status {@code 400 (Bad Request)} if the interventionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interventions/{id}")
    public ResponseEntity<InterventionDTO> updateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterventionDTO interventionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Intervention : {}, {}", id, interventionDTO);
        if (interventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterventionDTO result = interventionService.update(interventionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interventionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interventions/:id} : Partial updates given fields of an existing intervention, field will ignore if it is null
     *
     * @param id              the id of the interventionDTO to save.
     * @param interventionDTO the interventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionDTO,
     * or with status {@code 400 (Bad Request)} if the interventionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the interventionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the interventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interventions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterventionDTO> partialUpdateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterventionDTO interventionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intervention partially : {}, {}", id, interventionDTO);
        if (interventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterventionDTO> result = interventionService.partialUpdate(interventionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interventionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interventions} : get all the interventions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventions in body.
     */
    @GetMapping("/interventions")
    public ResponseEntity<List<InterventionDTO>> getAllInterventions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Interventions");
        Page<InterventionDTO> page = interventionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interventions/:id} : get the "id" intervention.
     *
     * @param id the id of the interventionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interventionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interventions/{id}")
    public ResponseEntity<InterventionDTO> getIntervention(@PathVariable Long id) {
        log.debug("REST request to get Intervention : {}", id);
        Optional<InterventionDTO> interventionDTO = interventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interventionDTO);
    }

    /**
     * {@code DELETE  /interventions/:id} : delete the "id" intervention.
     *
     * @param id the id of the interventionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interventions/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        log.debug("REST request to delete Intervention : {}", id);
        interventionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/interventions/download/{id}")
    public ResponseEntity<Resource> generateBonTickets(@PathVariable Long id) throws Exception {
        log.debug("REST download interventions  : {}", id);

        File file = interventionService.download(id);
        return getResourceResponseEntity(file);
    }

    public static ResponseEntity<Resource> getResourceResponseEntity(File file) {
        FileSystemResource resource = new FileSystemResource(file);
        // 2.
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(mediaType);
        // 3
        ContentDisposition disposition = ContentDisposition
            // 3.2
            .inline() // or .attachment()
            // 3.1
            .filename(Objects.requireNonNull(resource.getFilename()))
            .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
