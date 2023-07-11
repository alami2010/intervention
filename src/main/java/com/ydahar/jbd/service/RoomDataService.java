package com.ydahar.jbd.service;

import com.ydahar.jbd.service.dto.RoomDataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.ydahar.jbd.domain.RoomData}.
 */
public interface RoomDataService {
    /**
     * Save a roomData.
     *
     * @param roomDataDTO the entity to save.
     * @return the persisted entity.
     */
    RoomDataDTO save(RoomDataDTO roomDataDTO);

    /**
     * Updates a roomData.
     *
     * @param roomDataDTO the entity to update.
     * @return the persisted entity.
     */
    RoomDataDTO update(RoomDataDTO roomDataDTO);

    /**
     * Partially updates a roomData.
     *
     * @param roomDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomDataDTO> partialUpdate(RoomDataDTO roomDataDTO);

    /**
     * Get all the roomData.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomDataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" roomData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomDataDTO> findOne(Long id);

    /**
     * Delete the "id" roomData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
