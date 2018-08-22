package com.sensocon.core.web.rest;

import com.sensocon.core.SensoconServerApp;

import com.sensocon.core.domain.CompanySettings;
import com.sensocon.core.repository.CompanySettingsRepository;
import com.sensocon.core.repository.search.CompanySettingsSearchRepository;
import com.sensocon.core.service.CompanySettingsService;
import com.sensocon.core.service.dto.CompanySettingsDTO;
import com.sensocon.core.service.mapper.CompanySettingsMapper;
import com.sensocon.core.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.sensocon.core.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompanySettingsResource REST controller.
 *
 * @see CompanySettingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServerApp.class)
public class CompanySettingsResourceIntTest {

    private static final Long DEFAULT_DEFAULT_TIMEOUT_SECONDS = 1L;
    private static final Long UPDATED_DEFAULT_TIMEOUT_SECONDS = 2L;

    private static final Long DEFAULT_DEFAULT_SUPPRESSION_SECONDS = 1L;
    private static final Long UPDATED_DEFAULT_SUPPRESSION_SECONDS = 2L;

    @Autowired
    private CompanySettingsRepository companySettingsRepository;


    @Autowired
    private CompanySettingsMapper companySettingsMapper;
    

    @Autowired
    private CompanySettingsService companySettingsService;

    /**
     * This repository is mocked in the com.sensocon.core.repository.search test package.
     *
     * @see com.sensocon.core.repository.search.CompanySettingsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySettingsSearchRepository mockCompanySettingsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanySettingsMockMvc;

    private CompanySettings companySettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanySettingsResource companySettingsResource = new CompanySettingsResource(companySettingsService);
        this.restCompanySettingsMockMvc = MockMvcBuilders.standaloneSetup(companySettingsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanySettings createEntity(EntityManager em) {
        CompanySettings companySettings = new CompanySettings()
            .defaultTimeoutSeconds(DEFAULT_DEFAULT_TIMEOUT_SECONDS)
            .defaultSuppressionSeconds(DEFAULT_DEFAULT_SUPPRESSION_SECONDS);
        return companySettings;
    }

    @Before
    public void initTest() {
        companySettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanySettings() throws Exception {
        int databaseSizeBeforeCreate = companySettingsRepository.findAll().size();

        // Create the CompanySettings
        CompanySettingsDTO companySettingsDTO = companySettingsMapper.toDto(companySettings);
        restCompanySettingsMockMvc.perform(post("/api/company-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companySettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the CompanySettings in the database
        List<CompanySettings> companySettingsList = companySettingsRepository.findAll();
        assertThat(companySettingsList).hasSize(databaseSizeBeforeCreate + 1);
        CompanySettings testCompanySettings = companySettingsList.get(companySettingsList.size() - 1);
        assertThat(testCompanySettings.getDefaultTimeoutSeconds()).isEqualTo(DEFAULT_DEFAULT_TIMEOUT_SECONDS);
        assertThat(testCompanySettings.getDefaultSuppressionSeconds()).isEqualTo(DEFAULT_DEFAULT_SUPPRESSION_SECONDS);

        // Validate the CompanySettings in Elasticsearch
        verify(mockCompanySettingsSearchRepository, times(1)).save(testCompanySettings);
    }

    @Test
    @Transactional
    public void createCompanySettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companySettingsRepository.findAll().size();

        // Create the CompanySettings with an existing ID
        companySettings.setId(1L);
        CompanySettingsDTO companySettingsDTO = companySettingsMapper.toDto(companySettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanySettingsMockMvc.perform(post("/api/company-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companySettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompanySettings in the database
        List<CompanySettings> companySettingsList = companySettingsRepository.findAll();
        assertThat(companySettingsList).hasSize(databaseSizeBeforeCreate);

        // Validate the CompanySettings in Elasticsearch
        verify(mockCompanySettingsSearchRepository, times(0)).save(companySettings);
    }

    @Test
    @Transactional
    public void getAllCompanySettings() throws Exception {
        // Initialize the database
        companySettingsRepository.saveAndFlush(companySettings);

        // Get all the companySettingsList
        restCompanySettingsMockMvc.perform(get("/api/company-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companySettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultTimeoutSeconds").value(hasItem(DEFAULT_DEFAULT_TIMEOUT_SECONDS.intValue())))
            .andExpect(jsonPath("$.[*].defaultSuppressionSeconds").value(hasItem(DEFAULT_DEFAULT_SUPPRESSION_SECONDS.intValue())));
    }
    

    @Test
    @Transactional
    public void getCompanySettings() throws Exception {
        // Initialize the database
        companySettingsRepository.saveAndFlush(companySettings);

        // Get the companySettings
        restCompanySettingsMockMvc.perform(get("/api/company-settings/{id}", companySettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companySettings.getId().intValue()))
            .andExpect(jsonPath("$.defaultTimeoutSeconds").value(DEFAULT_DEFAULT_TIMEOUT_SECONDS.intValue()))
            .andExpect(jsonPath("$.defaultSuppressionSeconds").value(DEFAULT_DEFAULT_SUPPRESSION_SECONDS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCompanySettings() throws Exception {
        // Get the companySettings
        restCompanySettingsMockMvc.perform(get("/api/company-settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanySettings() throws Exception {
        // Initialize the database
        companySettingsRepository.saveAndFlush(companySettings);

        int databaseSizeBeforeUpdate = companySettingsRepository.findAll().size();

        // Update the companySettings
        CompanySettings updatedCompanySettings = companySettingsRepository.findById(companySettings.getId()).get();
        // Disconnect from session so that the updates on updatedCompanySettings are not directly saved in db
        em.detach(updatedCompanySettings);
        updatedCompanySettings
            .defaultTimeoutSeconds(UPDATED_DEFAULT_TIMEOUT_SECONDS)
            .defaultSuppressionSeconds(UPDATED_DEFAULT_SUPPRESSION_SECONDS);
        CompanySettingsDTO companySettingsDTO = companySettingsMapper.toDto(updatedCompanySettings);

        restCompanySettingsMockMvc.perform(put("/api/company-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companySettingsDTO)))
            .andExpect(status().isOk());

        // Validate the CompanySettings in the database
        List<CompanySettings> companySettingsList = companySettingsRepository.findAll();
        assertThat(companySettingsList).hasSize(databaseSizeBeforeUpdate);
        CompanySettings testCompanySettings = companySettingsList.get(companySettingsList.size() - 1);
        assertThat(testCompanySettings.getDefaultTimeoutSeconds()).isEqualTo(UPDATED_DEFAULT_TIMEOUT_SECONDS);
        assertThat(testCompanySettings.getDefaultSuppressionSeconds()).isEqualTo(UPDATED_DEFAULT_SUPPRESSION_SECONDS);

        // Validate the CompanySettings in Elasticsearch
        verify(mockCompanySettingsSearchRepository, times(1)).save(testCompanySettings);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanySettings() throws Exception {
        int databaseSizeBeforeUpdate = companySettingsRepository.findAll().size();

        // Create the CompanySettings
        CompanySettingsDTO companySettingsDTO = companySettingsMapper.toDto(companySettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCompanySettingsMockMvc.perform(put("/api/company-settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companySettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompanySettings in the database
        List<CompanySettings> companySettingsList = companySettingsRepository.findAll();
        assertThat(companySettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CompanySettings in Elasticsearch
        verify(mockCompanySettingsSearchRepository, times(0)).save(companySettings);
    }

    @Test
    @Transactional
    public void deleteCompanySettings() throws Exception {
        // Initialize the database
        companySettingsRepository.saveAndFlush(companySettings);

        int databaseSizeBeforeDelete = companySettingsRepository.findAll().size();

        // Get the companySettings
        restCompanySettingsMockMvc.perform(delete("/api/company-settings/{id}", companySettings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompanySettings> companySettingsList = companySettingsRepository.findAll();
        assertThat(companySettingsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CompanySettings in Elasticsearch
        verify(mockCompanySettingsSearchRepository, times(1)).deleteById(companySettings.getId());
    }

    @Test
    @Transactional
    public void searchCompanySettings() throws Exception {
        // Initialize the database
        companySettingsRepository.saveAndFlush(companySettings);
        when(mockCompanySettingsSearchRepository.search(queryStringQuery("id:" + companySettings.getId())))
            .thenReturn(Collections.singletonList(companySettings));
        // Search the companySettings
        restCompanySettingsMockMvc.perform(get("/api/_search/company-settings?query=id:" + companySettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companySettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultTimeoutSeconds").value(hasItem(DEFAULT_DEFAULT_TIMEOUT_SECONDS.intValue())))
            .andExpect(jsonPath("$.[*].defaultSuppressionSeconds").value(hasItem(DEFAULT_DEFAULT_SUPPRESSION_SECONDS.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanySettings.class);
        CompanySettings companySettings1 = new CompanySettings();
        companySettings1.setId(1L);
        CompanySettings companySettings2 = new CompanySettings();
        companySettings2.setId(companySettings1.getId());
        assertThat(companySettings1).isEqualTo(companySettings2);
        companySettings2.setId(2L);
        assertThat(companySettings1).isNotEqualTo(companySettings2);
        companySettings1.setId(null);
        assertThat(companySettings1).isNotEqualTo(companySettings2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanySettingsDTO.class);
        CompanySettingsDTO companySettingsDTO1 = new CompanySettingsDTO();
        companySettingsDTO1.setId(1L);
        CompanySettingsDTO companySettingsDTO2 = new CompanySettingsDTO();
        assertThat(companySettingsDTO1).isNotEqualTo(companySettingsDTO2);
        companySettingsDTO2.setId(companySettingsDTO1.getId());
        assertThat(companySettingsDTO1).isEqualTo(companySettingsDTO2);
        companySettingsDTO2.setId(2L);
        assertThat(companySettingsDTO1).isNotEqualTo(companySettingsDTO2);
        companySettingsDTO1.setId(null);
        assertThat(companySettingsDTO1).isNotEqualTo(companySettingsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(companySettingsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(companySettingsMapper.fromId(null)).isNull();
    }
}
