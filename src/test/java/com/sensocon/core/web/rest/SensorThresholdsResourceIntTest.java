package com.sensocon.core.web.rest;

import com.sensocon.core.SensoconServerApp;

import com.sensocon.core.domain.SensorThresholds;
import com.sensocon.core.repository.SensorThresholdsRepository;
import com.sensocon.core.repository.search.SensorThresholdsSearchRepository;
import com.sensocon.core.service.SensorThresholdsService;
import com.sensocon.core.service.dto.SensorThresholdsDTO;
import com.sensocon.core.service.mapper.SensorThresholdsMapper;
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
 * Test class for the SensorThresholdsResource REST controller.
 *
 * @see SensorThresholdsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServerApp.class)
public class SensorThresholdsResourceIntTest {

    @Autowired
    private SensorThresholdsRepository sensorThresholdsRepository;


    @Autowired
    private SensorThresholdsMapper sensorThresholdsMapper;
    

    @Autowired
    private SensorThresholdsService sensorThresholdsService;

    /**
     * This repository is mocked in the com.sensocon.core.repository.search test package.
     *
     * @see com.sensocon.core.repository.search.SensorThresholdsSearchRepositoryMockConfiguration
     */
    @Autowired
    private SensorThresholdsSearchRepository mockSensorThresholdsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorThresholdsMockMvc;

    private SensorThresholds sensorThresholds;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorThresholdsResource sensorThresholdsResource = new SensorThresholdsResource(sensorThresholdsService);
        this.restSensorThresholdsMockMvc = MockMvcBuilders.standaloneSetup(sensorThresholdsResource)
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
    public static SensorThresholds createEntity(EntityManager em) {
        SensorThresholds sensorThresholds = new SensorThresholds();
        return sensorThresholds;
    }

    @Before
    public void initTest() {
        sensorThresholds = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensorThresholds() throws Exception {
        int databaseSizeBeforeCreate = sensorThresholdsRepository.findAll().size();

        // Create the SensorThresholds
        SensorThresholdsDTO sensorThresholdsDTO = sensorThresholdsMapper.toDto(sensorThresholds);
        restSensorThresholdsMockMvc.perform(post("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdsDTO)))
            .andExpect(status().isCreated());

        // Validate the SensorThresholds in the database
        List<SensorThresholds> sensorThresholdsList = sensorThresholdsRepository.findAll();
        assertThat(sensorThresholdsList).hasSize(databaseSizeBeforeCreate + 1);
        SensorThresholds testSensorThresholds = sensorThresholdsList.get(sensorThresholdsList.size() - 1);

        // Validate the SensorThresholds in Elasticsearch
        verify(mockSensorThresholdsSearchRepository, times(1)).save(testSensorThresholds);
    }

    @Test
    @Transactional
    public void createSensorThresholdsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorThresholdsRepository.findAll().size();

        // Create the SensorThresholds with an existing ID
        sensorThresholds.setId(1L);
        SensorThresholdsDTO sensorThresholdsDTO = sensorThresholdsMapper.toDto(sensorThresholds);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorThresholdsMockMvc.perform(post("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorThresholds in the database
        List<SensorThresholds> sensorThresholdsList = sensorThresholdsRepository.findAll();
        assertThat(sensorThresholdsList).hasSize(databaseSizeBeforeCreate);

        // Validate the SensorThresholds in Elasticsearch
        verify(mockSensorThresholdsSearchRepository, times(0)).save(sensorThresholds);
    }

    @Test
    @Transactional
    public void getAllSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdsRepository.saveAndFlush(sensorThresholds);

        // Get all the sensorThresholdsList
        restSensorThresholdsMockMvc.perform(get("/api/sensor-thresholds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorThresholds.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdsRepository.saveAndFlush(sensorThresholds);

        // Get the sensorThresholds
        restSensorThresholdsMockMvc.perform(get("/api/sensor-thresholds/{id}", sensorThresholds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensorThresholds.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSensorThresholds() throws Exception {
        // Get the sensorThresholds
        restSensorThresholdsMockMvc.perform(get("/api/sensor-thresholds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdsRepository.saveAndFlush(sensorThresholds);

        int databaseSizeBeforeUpdate = sensorThresholdsRepository.findAll().size();

        // Update the sensorThresholds
        SensorThresholds updatedSensorThresholds = sensorThresholdsRepository.findById(sensorThresholds.getId()).get();
        // Disconnect from session so that the updates on updatedSensorThresholds are not directly saved in db
        em.detach(updatedSensorThresholds);
        SensorThresholdsDTO sensorThresholdsDTO = sensorThresholdsMapper.toDto(updatedSensorThresholds);

        restSensorThresholdsMockMvc.perform(put("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdsDTO)))
            .andExpect(status().isOk());

        // Validate the SensorThresholds in the database
        List<SensorThresholds> sensorThresholdsList = sensorThresholdsRepository.findAll();
        assertThat(sensorThresholdsList).hasSize(databaseSizeBeforeUpdate);
        SensorThresholds testSensorThresholds = sensorThresholdsList.get(sensorThresholdsList.size() - 1);

        // Validate the SensorThresholds in Elasticsearch
        verify(mockSensorThresholdsSearchRepository, times(1)).save(testSensorThresholds);
    }

    @Test
    @Transactional
    public void updateNonExistingSensorThresholds() throws Exception {
        int databaseSizeBeforeUpdate = sensorThresholdsRepository.findAll().size();

        // Create the SensorThresholds
        SensorThresholdsDTO sensorThresholdsDTO = sensorThresholdsMapper.toDto(sensorThresholds);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSensorThresholdsMockMvc.perform(put("/api/sensor-thresholds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorThresholdsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorThresholds in the database
        List<SensorThresholds> sensorThresholdsList = sensorThresholdsRepository.findAll();
        assertThat(sensorThresholdsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SensorThresholds in Elasticsearch
        verify(mockSensorThresholdsSearchRepository, times(0)).save(sensorThresholds);
    }

    @Test
    @Transactional
    public void deleteSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdsRepository.saveAndFlush(sensorThresholds);

        int databaseSizeBeforeDelete = sensorThresholdsRepository.findAll().size();

        // Get the sensorThresholds
        restSensorThresholdsMockMvc.perform(delete("/api/sensor-thresholds/{id}", sensorThresholds.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorThresholds> sensorThresholdsList = sensorThresholdsRepository.findAll();
        assertThat(sensorThresholdsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SensorThresholds in Elasticsearch
        verify(mockSensorThresholdsSearchRepository, times(1)).deleteById(sensorThresholds.getId());
    }

    @Test
    @Transactional
    public void searchSensorThresholds() throws Exception {
        // Initialize the database
        sensorThresholdsRepository.saveAndFlush(sensorThresholds);
        when(mockSensorThresholdsSearchRepository.search(queryStringQuery("id:" + sensorThresholds.getId())))
            .thenReturn(Collections.singletonList(sensorThresholds));
        // Search the sensorThresholds
        restSensorThresholdsMockMvc.perform(get("/api/_search/sensor-thresholds?query=id:" + sensorThresholds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorThresholds.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorThresholds.class);
        SensorThresholds sensorThresholds1 = new SensorThresholds();
        sensorThresholds1.setId(1L);
        SensorThresholds sensorThresholds2 = new SensorThresholds();
        sensorThresholds2.setId(sensorThresholds1.getId());
        assertThat(sensorThresholds1).isEqualTo(sensorThresholds2);
        sensorThresholds2.setId(2L);
        assertThat(sensorThresholds1).isNotEqualTo(sensorThresholds2);
        sensorThresholds1.setId(null);
        assertThat(sensorThresholds1).isNotEqualTo(sensorThresholds2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorThresholdsDTO.class);
        SensorThresholdsDTO sensorThresholdsDTO1 = new SensorThresholdsDTO();
        sensorThresholdsDTO1.setId(1L);
        SensorThresholdsDTO sensorThresholdsDTO2 = new SensorThresholdsDTO();
        assertThat(sensorThresholdsDTO1).isNotEqualTo(sensorThresholdsDTO2);
        sensorThresholdsDTO2.setId(sensorThresholdsDTO1.getId());
        assertThat(sensorThresholdsDTO1).isEqualTo(sensorThresholdsDTO2);
        sensorThresholdsDTO2.setId(2L);
        assertThat(sensorThresholdsDTO1).isNotEqualTo(sensorThresholdsDTO2);
        sensorThresholdsDTO1.setId(null);
        assertThat(sensorThresholdsDTO1).isNotEqualTo(sensorThresholdsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sensorThresholdsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sensorThresholdsMapper.fromId(null)).isNull();
    }
}
