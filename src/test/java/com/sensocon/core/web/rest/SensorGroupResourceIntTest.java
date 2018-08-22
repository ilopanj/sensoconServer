package com.sensocon.core.web.rest;

import com.sensocon.core.SensoconServerApp;

import com.sensocon.core.domain.SensorGroup;
import com.sensocon.core.repository.SensorGroupRepository;
import com.sensocon.core.repository.search.SensorGroupSearchRepository;
import com.sensocon.core.service.SensorGroupService;
import com.sensocon.core.service.dto.SensorGroupDTO;
import com.sensocon.core.service.mapper.SensorGroupMapper;
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
 * Test class for the SensorGroupResource REST controller.
 *
 * @see SensorGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServerApp.class)
public class SensorGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SensorGroupRepository sensorGroupRepository;


    @Autowired
    private SensorGroupMapper sensorGroupMapper;
    

    @Autowired
    private SensorGroupService sensorGroupService;

    /**
     * This repository is mocked in the com.sensocon.core.repository.search test package.
     *
     * @see com.sensocon.core.repository.search.SensorGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private SensorGroupSearchRepository mockSensorGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorGroupMockMvc;

    private SensorGroup sensorGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorGroupResource sensorGroupResource = new SensorGroupResource(sensorGroupService);
        this.restSensorGroupMockMvc = MockMvcBuilders.standaloneSetup(sensorGroupResource)
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
    public static SensorGroup createEntity(EntityManager em) {
        SensorGroup sensorGroup = new SensorGroup()
            .name(DEFAULT_NAME);
        return sensorGroup;
    }

    @Before
    public void initTest() {
        sensorGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensorGroup() throws Exception {
        int databaseSizeBeforeCreate = sensorGroupRepository.findAll().size();

        // Create the SensorGroup
        SensorGroupDTO sensorGroupDTO = sensorGroupMapper.toDto(sensorGroup);
        restSensorGroupMockMvc.perform(post("/api/sensor-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the SensorGroup in the database
        List<SensorGroup> sensorGroupList = sensorGroupRepository.findAll();
        assertThat(sensorGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SensorGroup testSensorGroup = sensorGroupList.get(sensorGroupList.size() - 1);
        assertThat(testSensorGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the SensorGroup in Elasticsearch
        verify(mockSensorGroupSearchRepository, times(1)).save(testSensorGroup);
    }

    @Test
    @Transactional
    public void createSensorGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorGroupRepository.findAll().size();

        // Create the SensorGroup with an existing ID
        sensorGroup.setId(1L);
        SensorGroupDTO sensorGroupDTO = sensorGroupMapper.toDto(sensorGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorGroupMockMvc.perform(post("/api/sensor-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorGroup in the database
        List<SensorGroup> sensorGroupList = sensorGroupRepository.findAll();
        assertThat(sensorGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the SensorGroup in Elasticsearch
        verify(mockSensorGroupSearchRepository, times(0)).save(sensorGroup);
    }

    @Test
    @Transactional
    public void getAllSensorGroups() throws Exception {
        // Initialize the database
        sensorGroupRepository.saveAndFlush(sensorGroup);

        // Get all the sensorGroupList
        restSensorGroupMockMvc.perform(get("/api/sensor-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getSensorGroup() throws Exception {
        // Initialize the database
        sensorGroupRepository.saveAndFlush(sensorGroup);

        // Get the sensorGroup
        restSensorGroupMockMvc.perform(get("/api/sensor-groups/{id}", sensorGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensorGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSensorGroup() throws Exception {
        // Get the sensorGroup
        restSensorGroupMockMvc.perform(get("/api/sensor-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensorGroup() throws Exception {
        // Initialize the database
        sensorGroupRepository.saveAndFlush(sensorGroup);

        int databaseSizeBeforeUpdate = sensorGroupRepository.findAll().size();

        // Update the sensorGroup
        SensorGroup updatedSensorGroup = sensorGroupRepository.findById(sensorGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSensorGroup are not directly saved in db
        em.detach(updatedSensorGroup);
        updatedSensorGroup
            .name(UPDATED_NAME);
        SensorGroupDTO sensorGroupDTO = sensorGroupMapper.toDto(updatedSensorGroup);

        restSensorGroupMockMvc.perform(put("/api/sensor-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorGroupDTO)))
            .andExpect(status().isOk());

        // Validate the SensorGroup in the database
        List<SensorGroup> sensorGroupList = sensorGroupRepository.findAll();
        assertThat(sensorGroupList).hasSize(databaseSizeBeforeUpdate);
        SensorGroup testSensorGroup = sensorGroupList.get(sensorGroupList.size() - 1);
        assertThat(testSensorGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the SensorGroup in Elasticsearch
        verify(mockSensorGroupSearchRepository, times(1)).save(testSensorGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingSensorGroup() throws Exception {
        int databaseSizeBeforeUpdate = sensorGroupRepository.findAll().size();

        // Create the SensorGroup
        SensorGroupDTO sensorGroupDTO = sensorGroupMapper.toDto(sensorGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restSensorGroupMockMvc.perform(put("/api/sensor-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensorGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SensorGroup in the database
        List<SensorGroup> sensorGroupList = sensorGroupRepository.findAll();
        assertThat(sensorGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SensorGroup in Elasticsearch
        verify(mockSensorGroupSearchRepository, times(0)).save(sensorGroup);
    }

    @Test
    @Transactional
    public void deleteSensorGroup() throws Exception {
        // Initialize the database
        sensorGroupRepository.saveAndFlush(sensorGroup);

        int databaseSizeBeforeDelete = sensorGroupRepository.findAll().size();

        // Get the sensorGroup
        restSensorGroupMockMvc.perform(delete("/api/sensor-groups/{id}", sensorGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SensorGroup> sensorGroupList = sensorGroupRepository.findAll();
        assertThat(sensorGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SensorGroup in Elasticsearch
        verify(mockSensorGroupSearchRepository, times(1)).deleteById(sensorGroup.getId());
    }

    @Test
    @Transactional
    public void searchSensorGroup() throws Exception {
        // Initialize the database
        sensorGroupRepository.saveAndFlush(sensorGroup);
        when(mockSensorGroupSearchRepository.search(queryStringQuery("id:" + sensorGroup.getId())))
            .thenReturn(Collections.singletonList(sensorGroup));
        // Search the sensorGroup
        restSensorGroupMockMvc.perform(get("/api/_search/sensor-groups?query=id:" + sensorGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensorGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorGroup.class);
        SensorGroup sensorGroup1 = new SensorGroup();
        sensorGroup1.setId(1L);
        SensorGroup sensorGroup2 = new SensorGroup();
        sensorGroup2.setId(sensorGroup1.getId());
        assertThat(sensorGroup1).isEqualTo(sensorGroup2);
        sensorGroup2.setId(2L);
        assertThat(sensorGroup1).isNotEqualTo(sensorGroup2);
        sensorGroup1.setId(null);
        assertThat(sensorGroup1).isNotEqualTo(sensorGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensorGroupDTO.class);
        SensorGroupDTO sensorGroupDTO1 = new SensorGroupDTO();
        sensorGroupDTO1.setId(1L);
        SensorGroupDTO sensorGroupDTO2 = new SensorGroupDTO();
        assertThat(sensorGroupDTO1).isNotEqualTo(sensorGroupDTO2);
        sensorGroupDTO2.setId(sensorGroupDTO1.getId());
        assertThat(sensorGroupDTO1).isEqualTo(sensorGroupDTO2);
        sensorGroupDTO2.setId(2L);
        assertThat(sensorGroupDTO1).isNotEqualTo(sensorGroupDTO2);
        sensorGroupDTO1.setId(null);
        assertThat(sensorGroupDTO1).isNotEqualTo(sensorGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sensorGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sensorGroupMapper.fromId(null)).isNull();
    }
}
