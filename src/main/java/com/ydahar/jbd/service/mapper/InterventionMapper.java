package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.Intervention;
import com.ydahar.jbd.service.dto.InterventionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intervention} and its DTO {@link InterventionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterventionMapper extends EntityMapper<InterventionDTO, Intervention> {}
