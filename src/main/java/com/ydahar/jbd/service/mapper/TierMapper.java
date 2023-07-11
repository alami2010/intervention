package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.Floor;
import com.ydahar.jbd.domain.Tier;
import com.ydahar.jbd.service.dto.FloorDTO;
import com.ydahar.jbd.service.dto.TierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tier} and its DTO {@link TierDTO}.
 */
@Mapper(componentModel = "spring")
public interface TierMapper extends EntityMapper<TierDTO, Tier> {
    TierDTO toDto(Tier s);
}
