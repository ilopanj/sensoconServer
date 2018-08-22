package com.sensocon.core.repository.search;

import com.sensocon.core.domain.SensorThreshold;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SensorThreshold entity.
 */
public interface SensorThresholdSearchRepository extends ElasticsearchRepository<SensorThreshold, Long> {
}
