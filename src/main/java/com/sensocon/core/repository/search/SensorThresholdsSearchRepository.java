package com.sensocon.core.repository.search;

import com.sensocon.core.domain.SensorThresholds;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SensorThresholds entity.
 */
public interface SensorThresholdsSearchRepository extends ElasticsearchRepository<SensorThresholds, Long> {
}
