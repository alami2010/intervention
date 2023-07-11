package com.ydahar.jbd.service.impl;

import com.ydahar.jbd.domain.Floor;
import com.ydahar.jbd.repository.FloorRepository;
import com.ydahar.jbd.service.FloorService;
import com.ydahar.jbd.service.dto.FloorDTO;
import com.ydahar.jbd.service.mapper.FloorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Floor}.
 */
@Service
@Transactional
public class FloorServiceImpl implements FloorService {

    private final Logger log = LoggerFactory.getLogger(FloorServiceImpl.class);

    private final FloorRepository floorRepository;

    private final FloorMapper floorMapper;

    public FloorServiceImpl(FloorRepository floorRepository, FloorMapper floorMapper) {
        this.floorRepository = floorRepository;
        this.floorMapper = floorMapper;
    }

    @Override
    public FloorDTO save(FloorDTO floorDTO) {
        log.debug("Request to save Floor : {}", floorDTO);
        Floor floor = floorMapper.toEntity(floorDTO);
        floor = floorRepository.save(floor);
        return floorMapper.toDto(floor);
    }

    @Override
    public FloorDTO update(FloorDTO floorDTO) {
        log.debug("Request to update Floor : {}", floorDTO);
        Floor floor = floorMapper.toEntity(floorDTO);
        floor = floorRepository.save(floor);
        return floorMapper.toDto(floor);
    }

    @Override
    public Optional<FloorDTO> partialUpdate(FloorDTO floorDTO) {
        log.debug("Request to partially update Floor : {}", floorDTO);

        return floorRepository
            .findById(floorDTO.getId())
            .map(existingFloor -> {
                floorMapper.partialUpdate(existingFloor, floorDTO);

                return existingFloor;
            })
            .map(floorRepository::save)
            .map(floorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FloorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Floors");
        return floorRepository.findAll(pageable).map(floorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FloorDTO> findOne(Long id) {
        log.debug("Request to get Floor : {}", id);
        return floorRepository.findById(id).map(floorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Floor : {}", id);
        floorRepository.deleteById(id);
    }
}
