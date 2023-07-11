package com.ydahar.jbd.service;

import com.ydahar.jbd.service.dto.TierDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ydahar.jbd.domain.TierData}.
 */
public interface TierDataService {
    /**
     * Save a tierData.
     *
     * @param tierDataDTO the entity to save.
     * @return the persisted entity.
     */
    TierDataDTO save(TierDataDTO tierDataDTO);

    /**
     * Updates a tierData.
     *
     * @param tierDataDTO the entity to update.
     * @return the persisted entity.
     */
    TierDataDTO update(TierDataDTO tierDataDTO);

    /**
     * Partially updates a tierData.
     *
     * @param tierDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TierDataDTO> partialUpdate(TierDataDTO tierDataDTO);

    /**
     * Get all the tierData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TierDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tierData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TierDataDTO> findOne(Long id);

    /**
     * Delete the "id" tierData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
