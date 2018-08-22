package com.sensocon.core.repository;

import com.sensocon.core.domain.SensorGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SensorGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensorGroupRepository extends JpaRepository<SensorGroup, Long> {

}
