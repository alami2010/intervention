package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.FloorData;
import com.ydahar.jbd.service.dto.FloorDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FloorData} and its DTO {@link FloorDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface FloorDataMapper extends EntityMapper<FloorDataDTO, FloorData> {}
