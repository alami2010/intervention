package com.ydahar.jbd.service;

import com.ydahar.jbd.service.dto.TierDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ydahar.jbd.domain.Tier}.
 */
public interface TierService {
    /**
     * Save a tier.
     *
     * @param tierDTO the entity to save.
     * @return the persisted entity.
     */
    TierDTO save(TierDTO tierDTO);

    /**
     * Updates a tier.
     *
     * @param tierDTO the entity to update.
     * @return the persisted entity.
     */
    TierDTO update(TierDTO tierDTO);

    /**
     * Partially updates a tier.
     *
     * @param tierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TierDTO> partialUpdate(TierDTO tierDTO);

    /**
     * Get all the tiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TierDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TierDTO> findOne(Long id);

    /**
     * Delete the "id" tier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
