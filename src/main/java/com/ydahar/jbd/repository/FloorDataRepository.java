package com.ydahar.jbd.repository;

import com.ydahar.jbd.domain.FloorData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FloorData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloorDataRepository extends JpaRepository<FloorData, Long> {}
