package com.ydahar.jbd.service.impl;

import com.ydahar.jbd.domain.Tier;
import com.ydahar.jbd.repository.TierRepository;
import com.ydahar.jbd.service.TierService;
import com.ydahar.jbd.service.dto.TierDTO;
import com.ydahar.jbd.service.mapper.TierMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tier}.
 */
@Service
@Transactional
public class TierServiceImpl implements TierService {

    private final Logger log = LoggerFactory.getLogger(TierServiceImpl.class);

    private final TierRepository tierRepository;

    private final TierMapper tierMapper;

    public TierServiceImpl(TierRepository tierRepository, TierMapper tierMapper) {
        this.tierRepository = tierRepository;
        this.tierMapper = tierMapper;
    }

    @Override
    public TierDTO save(TierDTO tierDTO) {
        log.debug("Request to save Tier : {}", tierDTO);
        Tier tier = tierMapper.toEntity(tierDTO);
        tier = tierRepository.save(tier);
        return tierMapper.toDto(tier);
    }

    @Override
    public TierDTO update(TierDTO tierDTO) {
        log.debug("Request to update Tier : {}", tierDTO);
        Tier tier = tierMapper.toEntity(tierDTO);
        tier = tierRepository.save(tier);
        return tierMapper.toDto(tier);
    }

    @Override
    public Optional<TierDTO> partialUpdate(TierDTO tierDTO) {
        log.debug("Request to partially update Tier : {}", tierDTO);

        return tierRepository
            .findById(tierDTO.getId())
            .map(existingTier -> {
                tierMapper.partialUpdate(existingTier, tierDTO);

                return existingTier;
            })
            .map(tierRepository::save)
            .map(tierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tiers");
        return tierRepository.findAll(pageable).map(tierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TierDTO> findOne(Long id) {
        log.debug("Request to get Tier : {}", id);
        return tierRepository.findById(id).map(tierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tier : {}", id);
        tierRepository.deleteById(id);
    }
}
