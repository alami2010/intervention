package com.ydahar.jbd.service.impl;

import com.ydahar.jbd.domain.TierData;
import com.ydahar.jbd.repository.TierDataRepository;
import com.ydahar.jbd.service.TierDataService;
import com.ydahar.jbd.service.dto.TierDataDTO;
import com.ydahar.jbd.service.mapper.TierDataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TierData}.
 */
@Service
@Transactional
public class TierDataServiceImpl implements TierDataService {

    private final Logger log = LoggerFactory.getLogger(TierDataServiceImpl.class);

    private final TierDataRepository tierDataRepository;

    private final TierDataMapper tierDataMapper;

    public TierDataServiceImpl(TierDataRepository tierDataRepository, TierDataMapper tierDataMapper) {
        this.tierDataRepository = tierDataRepository;
        this.tierDataMapper = tierDataMapper;
    }

    @Override
    public TierDataDTO save(TierDataDTO tierDataDTO) {
        log.debug("Request to save TierData : {}", tierDataDTO);
        TierData tierData = tierDataMapper.toEntity(tierDataDTO);
        tierData = tierDataRepository.save(tierData);
        return tierDataMapper.toDto(tierData);
    }

    @Override
    public TierDataDTO update(TierDataDTO tierDataDTO) {
        log.debug("Request to update TierData : {}", tierDataDTO);
        TierData tierData = tierDataMapper.toEntity(tierDataDTO);
        tierData = tierDataRepository.save(tierData);
        return tierDataMapper.toDto(tierData);
    }

    @Override
    public Optional<TierDataDTO> partialUpdate(TierDataDTO tierDataDTO) {
        log.debug("Request to partially update TierData : {}", tierDataDTO);

        return tierDataRepository
            .findById(tierDataDTO.getId())
            .map(existingTierData -> {
                tierDataMapper.partialUpdate(existingTierData, tierDataDTO);

                return existingTierData;
            })
            .map(tierDataRepository::save)
            .map(tierDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TierDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TierData");
        return tierDataRepository.findAll(pageable).map(tierDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TierDataDTO> findOne(Long id) {
        log.debug("Request to get TierData : {}", id);
        return tierDataRepository.findById(id).map(tierDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TierData : {}", id);
        tierDataRepository.deleteById(id);
    }
}
