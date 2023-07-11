package com.ydahar.jbd.repository;

import com.ydahar.jbd.domain.TierData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TierData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TierDataRepository extends JpaRepository<TierData, Long> {}
