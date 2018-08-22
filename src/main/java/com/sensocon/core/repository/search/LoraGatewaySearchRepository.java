package com.sensocon.core.repository.search;

import com.sensocon.core.domain.LoraGateway;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LoraGateway entity.
 */
public interface LoraGatewaySearchRepository extends ElasticsearchRepository<LoraGateway, Long> {
}
