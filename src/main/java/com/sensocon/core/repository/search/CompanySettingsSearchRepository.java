package com.sensocon.core.repository.search;

import com.sensocon.core.domain.CompanySettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanySettings entity.
 */
public interface CompanySettingsSearchRepository extends ElasticsearchRepository<CompanySettings, Long> {
}
