package com.sensocon.core.repository.search;

import com.sensocon.core.domain.Sensor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Sensor entity.
 */
public interface SensorSearchRepository extends ElasticsearchRepository<Sensor, Long> {
}
