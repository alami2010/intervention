package com.ydahar.jbd.repository;

import com.ydahar.jbd.domain.Tier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TierRepository extends JpaRepository<Tier, Long> {}
