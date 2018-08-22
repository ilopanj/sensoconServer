package com.sensocon.core.repository.search;

import com.sensocon.core.domain.SensorGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SensorGroup entity.
 */
public interface SensorGroupSearchRepository extends ElasticsearchRepository<SensorGroup, Long> {
}
