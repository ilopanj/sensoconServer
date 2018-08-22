package com.sensocon.core.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of LoraPacketSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LoraPacketSearchRepositoryMockConfiguration {

    @MockBean
    private LoraPacketSearchRepository mockLoraPacketSearchRepository;

}
