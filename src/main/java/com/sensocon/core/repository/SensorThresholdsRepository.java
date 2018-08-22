package com.sensocon.core.repository;

import com.sensocon.core.domain.SensorThresholds;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SensorThresholds entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorThresholdsRepository extends JpaRepository<SensorThresholds, Long> {

}
