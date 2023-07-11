package com.ydahar.jbd.service.impl;

import com.ydahar.jbd.domain.FloorData;
import com.ydahar.jbd.repository.FloorDataRepository;
import com.ydahar.jbd.service.FloorDataService;
import com.ydahar.jbd.service.dto.FloorDataDTO;
import com.ydahar.jbd.service.mapper.FloorDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FloorData}.
 */
@Service
@Transactional
public class FloorDataServiceImpl implements FloorDataService {

    private final Logger log = LoggerFactory.getLogger(FloorDataServiceImpl.class);

    private final FloorDataRepository floorDataRepository;

    private final FloorDataMapper floorDataMapper;

    public FloorDataServiceImpl(FloorDataRepository floorDataRepository, FloorDataMapper floorDataMapper) {
        this.floorDataRepository = floorDataRepository;
        this.floorDataMapper = floorDataMapper;
    }

    @Override
    public FloorDataDTO save(FloorDataDTO floorDataDTO) {
        log.debug("Request to save FloorData : {}", floorDataDTO);
        FloorData floorData = floorDataMapper.toEntity(floorDataDTO);
        floorData = floorDataRepository.save(floorData);
        return floorDataMapper.toDto(floorData);
    }

    @Override
    public FloorDataDTO update(FloorDataDTO floorDataDTO) {
        log.debug("Request to update FloorData : {}", floorDataDTO);
        FloorData floorData = floorDataMapper.toEntity(floorDataDTO);
        floorData = floorDataRepository.save(floorData);
        return floorDataMapper.toDto(floorData);
    }

    @Override
    public Optional<FloorDataDTO> partialUpdate(FloorDataDTO floorDataDTO) {
        log.debug("Request to partially update FloorData : {}", floorDataDTO);

        return floorDataRepository
            .findById(floorDataDTO.getId())
            .map(existingFloorData -> {
                floorDataMapper.partialUpdate(existingFloorData, floorDataDTO);

                return existingFloorData;
            })
            .map(floorDataRepository::save)
            .map(floorDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FloorDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FloorData");
        return floorDataRepository.findAll(pageable).map(floorDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FloorDataDTO> findOne(Long id) {
        log.debug("Request to get FloorData : {}", id);
        return floorDataRepository.findById(id).map(floorDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FloorData : {}", id);
        floorDataRepository.deleteById(id);
    }
}
