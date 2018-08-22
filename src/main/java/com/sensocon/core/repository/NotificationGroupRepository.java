package com.sensocon.core.repository;

import com.sensocon.core.domain.NotificationGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NotificationGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long> {

}
