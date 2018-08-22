package com.sensocon.core.repository.search;

import com.sensocon.core.domain.LoraPacket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LoraPacket entity.
 */
public interface LoraPacketSearchRepository extends ElasticsearchRepository<LoraPacket, Long> {
}
