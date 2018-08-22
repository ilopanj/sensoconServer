package com.sensocon.core.repository.search;

import com.sensocon.core.domain.SensorDevice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SensorDevice entity.
 */
public interface SensorDeviceSearchRepository extends ElasticsearchRepository<SensorDevice, Long> {
}
