package com.ydahar.jbd.service;

import com.ydahar.jbd.service.dto.FloorDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ydahar.jbd.domain.FloorData}.
 */
public interface FloorDataService {
    /**
     * Save a floorData.
     *
     * @param floorDataDTO the entity to save.
     * @return the persisted entity.
     */
    FloorDataDTO save(FloorDataDTO floorDataDTO);

    /**
     * Updates a floorData.
     *
     * @param floorDataDTO the entity to update.
     * @return the persisted entity.
     */
    FloorDataDTO update(FloorDataDTO floorDataDTO);

    /**
     * Partially updates a floorData.
     *
     * @param floorDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FloorDataDTO> partialUpdate(FloorDataDTO floorDataDTO);

    /**
     * Get all the floorData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FloorDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" floorData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FloorDataDTO> findOne(Long id);

    /**
     * Delete the "id" floorData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
