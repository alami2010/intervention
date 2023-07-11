package com.ydahar.jbd.service.impl;

import com.ydahar.jbd.domain.RoomData;
import com.ydahar.jbd.repository.RoomDataRepository;
import com.ydahar.jbd.service.RoomDataService;
import com.ydahar.jbd.service.dto.RoomDataDTO;
import com.ydahar.jbd.service.mapper.RoomDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoomData}.
 */
@Service
@Transactional
public class RoomDataServiceImpl implements RoomDataService {

    private final Logger log = LoggerFactory.getLogger(RoomDataServiceImpl.class);

    private final RoomDataRepository roomDataRepository;

    private final RoomDataMapper roomDataMapper;

    public RoomDataServiceImpl(RoomDataRepository roomDataRepository, RoomDataMapper roomDataMapper) {
        this.roomDataRepository = roomDataRepository;
        this.roomDataMapper = roomDataMapper;
    }

    @Override
    public RoomDataDTO save(RoomDataDTO roomDataDTO) {
        log.debug("Request to save RoomData : {}", roomDataDTO);
        RoomData roomData = roomDataMapper.toEntity(roomDataDTO);
        roomData = roomDataRepository.save(roomData);
        return roomDataMapper.toDto(roomData);
    }

    @Override
    public RoomDataDTO update(RoomDataDTO roomDataDTO) {
        log.debug("Request to update RoomData : {}", roomDataDTO);
        RoomData roomData = roomDataMapper.toEntity(roomDataDTO);
        roomData = roomDataRepository.save(roomData);
        return roomDataMapper.toDto(roomData);
    }

    @Override
    public Optional<RoomDataDTO> partialUpdate(RoomDataDTO roomDataDTO) {
        log.debug("Request to partially update RoomData : {}", roomDataDTO);

        return roomDataRepository
            .findById(roomDataDTO.getId())
            .map(existingRoomData -> {
                roomDataMapper.partialUpdate(existingRoomData, roomDataDTO);

                return existingRoomData;
            })
            .map(roomDataRepository::save)
            .map(roomDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomData");
        return roomDataRepository.findAll(pageable).map(roomDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDataDTO> findOne(Long id) {
        log.debug("Request to get RoomData : {}", id);
        return roomDataRepository.findById(id).map(roomDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoomData : {}", id);
        roomDataRepository.deleteById(id);
    }
}
