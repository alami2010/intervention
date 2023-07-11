package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.Floor;
import com.ydahar.jbd.domain.Intervention;
import com.ydahar.jbd.service.dto.FloorDTO;
import com.ydahar.jbd.service.dto.InterventionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Floor} and its DTO {@link FloorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FloorMapper extends EntityMapper<FloorDTO, Floor> {
    FloorDTO toDto(Floor s);
}
