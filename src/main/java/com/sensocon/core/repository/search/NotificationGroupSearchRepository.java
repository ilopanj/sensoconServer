package com.sensocon.core.repository.search;

import com.sensocon.core.domain.NotificationGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the NotificationGroup entity.
 */
public interface NotificationGroupSearchRepository extends ElasticsearchRepository<NotificationGroup, Long> {
}
