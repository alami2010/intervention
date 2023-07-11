package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.RoomData;
import com.ydahar.jbd.domain.TierData;
import com.ydahar.jbd.service.dto.RoomDataDTO;
import com.ydahar.jbd.service.dto.TierDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoomData} and its DTO {@link RoomDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomDataMapper extends EntityMapper<RoomDataDTO, RoomData> {
    @Mapping(target = "tier", source = "tier", qualifiedByName = "tierDataId")
    RoomDataDTO toDto(RoomData s);

    @Named("tierDataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TierDataDTO toDtoTierDataId(TierData tierData);
}
