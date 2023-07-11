package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.FloorData;
import com.ydahar.jbd.domain.TierData;
import com.ydahar.jbd.service.dto.FloorDataDTO;
import com.ydahar.jbd.service.dto.TierDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TierData} and its DTO {@link TierDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface TierDataMapper extends EntityMapper<TierDataDTO, TierData> {
    @Mapping(target = "floor", source = "floor", qualifiedByName = "floorDataId")
    TierDataDTO toDto(TierData s);

    @Named("floorDataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FloorDataDTO toDtoFloorDataId(FloorData floorData);
}
