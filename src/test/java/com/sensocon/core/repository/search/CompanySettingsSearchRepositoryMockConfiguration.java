package com.sensocon.core.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CompanySettingsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CompanySettingsSearchRepositoryMockConfiguration {

    @MockBean
    private CompanySettingsSearchRepository mockCompanySettingsSearchRepository;

}
