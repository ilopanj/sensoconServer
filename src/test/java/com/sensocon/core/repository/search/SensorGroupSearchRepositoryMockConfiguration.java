package com.sensocon.core.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SensorGroupSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SensorGroupSearchRepositoryMockConfiguration {

    @MockBean
    private SensorGroupSearchRepository mockSensorGroupSearchRepository;

}
