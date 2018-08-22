package com.sensocon.core.repository;

import com.sensocon.core.domain.SensorDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SensorDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorDeviceRepository extends JpaRepository<SensorDevice, Long> {

}
