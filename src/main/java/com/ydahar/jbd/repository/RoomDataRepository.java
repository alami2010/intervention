package com.ydahar.jbd.repository;

import com.ydahar.jbd.domain.RoomData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomDataRepository extends JpaRepository<RoomData, Long> {}
