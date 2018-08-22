package com.sensocon.core.repository;

import com.sensocon.core.domain.SensorThreshold;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SensorThreshold entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorThresholdRepository extends JpaRepository<SensorThreshold, Long> {

}
