package com.ydahar.jbd.service;

import com.lowagie.text.DocumentException;
import com.ydahar.jbd.service.dto.InterventionDTO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ydahar.jbd.domain.Intervention}.
 */
public interface InterventionService {
    /**
     * Save a intervention.
     *
     * @param interventionDTO the entity to save.
     * @return the persisted entity.
     */
    InterventionDTO save(InterventionDTO interventionDTO) throws DocumentException, IOException;

    /**
     * Updates a intervention.
     *
     * @param interventionDTO the entity to update.
     * @return the persisted entity.
     */
    InterventionDTO update(InterventionDTO interventionDTO);

    /**
     * Partially updates a intervention.
     *
     * @param interventionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InterventionDTO> partialUpdate(InterventionDTO interventionDTO);

    /**
     * Get all the interventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InterventionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" intervention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterventionDTO> findOne(Long id);

    /**
     * Delete the "id" intervention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    File download(Long id) throws DocumentException, IOException;
}
