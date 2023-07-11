package com.ydahar.jbd.service.mapper;

import com.ydahar.jbd.domain.Room;
import com.ydahar.jbd.domain.Tier;
import com.ydahar.jbd.service.dto.RoomDTO;
import com.ydahar.jbd.service.dto.TierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    RoomDTO toDto(Room s);
}
