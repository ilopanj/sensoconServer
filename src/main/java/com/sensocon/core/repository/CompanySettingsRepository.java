package com.sensocon.core.repository;

import com.sensocon.core.domain.CompanySettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanySettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanySettingsRepository extends JpaRepository<CompanySettings, Long> {

}
