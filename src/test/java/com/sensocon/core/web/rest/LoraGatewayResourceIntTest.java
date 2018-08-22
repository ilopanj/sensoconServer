package com.sensocon.core.web.rest;

import com.sensocon.core.SensoconServerApp;

import com.sensocon.core.domain.LoraGateway;
import com.sensocon.core.domain.Location;
import com.sensocon.core.repository.LoraGatewayRepository;
import com.sensocon.core.repository.search.LoraGatewaySearchRepository;
import com.sensocon.core.service.LoraGatewayService;
import com.sensocon.core.service.dto.LoraGatewayDTO;
import com.sensocon.core.service.mapper.LoraGatewayMapper;
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
 * Test class for the LoraGatewayResource REST controller.
 *
 * @see LoraGatewayResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServerApp.class)
public class LoraGatewayResourceIntTest {

    private static final String DEFAULT_GATEWAY_ID = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private LoraGatewayRepository loraGatewayRepository;


    @Autowired
    private LoraGatewayMapper loraGatewayMapper;
    

    @Autowired
    private LoraGatewayService loraGatewayService;

    /**
     * This repository is mocked in the com.sensocon.core.repository.search test package.
     *
     * @see com.sensocon.core.repository.search.LoraGatewaySearchRepositoryMockConfiguration
     */
    @Autowired
    private LoraGatewaySearchRepository mockLoraGatewaySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLoraGatewayMockMvc;

    private LoraGateway loraGateway;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoraGatewayResource loraGatewayResource = new LoraGatewayResource(loraGatewayService);
        this.restLoraGatewayMockMvc = MockMvcBuilders.standaloneSetup(loraGatewayResource)
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
    public static LoraGateway createEntity(EntityManager em) {
        LoraGateway loraGateway = new LoraGateway()
            .gatewayId(DEFAULT_GATEWAY_ID)
            .name(DEFAULT_NAME);
        // Add required entity
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        loraGateway.setLocation(location);
        return loraGateway;
    }

    @Before
    public void initTest() {
        loraGateway = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoraGateway() throws Exception {
        int databaseSizeBeforeCreate = loraGatewayRepository.findAll().size();

        // Create the LoraGateway
        LoraGatewayDTO loraGatewayDTO = loraGatewayMapper.toDto(loraGateway);
        restLoraGatewayMockMvc.perform(post("/api/lora-gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraGatewayDTO)))
            .andExpect(status().isCreated());

        // Validate the LoraGateway in the database
        List<LoraGateway> loraGatewayList = loraGatewayRepository.findAll();
        assertThat(loraGatewayList).hasSize(databaseSizeBeforeCreate + 1);
        LoraGateway testLoraGateway = loraGatewayList.get(loraGatewayList.size() - 1);
        assertThat(testLoraGateway.getGatewayId()).isEqualTo(DEFAULT_GATEWAY_ID);
        assertThat(testLoraGateway.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the LoraGateway in Elasticsearch
        verify(mockLoraGatewaySearchRepository, times(1)).save(testLoraGateway);
    }

    @Test
    @Transactional
    public void createLoraGatewayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loraGatewayRepository.findAll().size();

        // Create the LoraGateway with an existing ID
        loraGateway.setId(1L);
        LoraGatewayDTO loraGatewayDTO = loraGatewayMapper.toDto(loraGateway);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoraGatewayMockMvc.perform(post("/api/lora-gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraGatewayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoraGateway in the database
        List<LoraGateway> loraGatewayList = loraGatewayRepository.findAll();
        assertThat(loraGatewayList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoraGateway in Elasticsearch
        verify(mockLoraGatewaySearchRepository, times(0)).save(loraGateway);
    }

    @Test
    @Transactional
    public void getAllLoraGateways() throws Exception {
        // Initialize the database
        loraGatewayRepository.saveAndFlush(loraGateway);

        // Get all the loraGatewayList
        restLoraGatewayMockMvc.perform(get("/api/lora-gateways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loraGateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].gatewayId").value(hasItem(DEFAULT_GATEWAY_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getLoraGateway() throws Exception {
        // Initialize the database
        loraGatewayRepository.saveAndFlush(loraGateway);

        // Get the loraGateway
        restLoraGatewayMockMvc.perform(get("/api/lora-gateways/{id}", loraGateway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loraGateway.getId().intValue()))
            .andExpect(jsonPath("$.gatewayId").value(DEFAULT_GATEWAY_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingLoraGateway() throws Exception {
        // Get the loraGateway
        restLoraGatewayMockMvc.perform(get("/api/lora-gateways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoraGateway() throws Exception {
        // Initialize the database
        loraGatewayRepository.saveAndFlush(loraGateway);

        int databaseSizeBeforeUpdate = loraGatewayRepository.findAll().size();

        // Update the loraGateway
        LoraGateway updatedLoraGateway = loraGatewayRepository.findById(loraGateway.getId()).get();
        // Disconnect from session so that the updates on updatedLoraGateway are not directly saved in db
        em.detach(updatedLoraGateway);
        updatedLoraGateway
            .gatewayId(UPDATED_GATEWAY_ID)
            .name(UPDATED_NAME);
        LoraGatewayDTO loraGatewayDTO = loraGatewayMapper.toDto(updatedLoraGateway);

        restLoraGatewayMockMvc.perform(put("/api/lora-gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraGatewayDTO)))
            .andExpect(status().isOk());

        // Validate the LoraGateway in the database
        List<LoraGateway> loraGatewayList = loraGatewayRepository.findAll();
        assertThat(loraGatewayList).hasSize(databaseSizeBeforeUpdate);
        LoraGateway testLoraGateway = loraGatewayList.get(loraGatewayList.size() - 1);
        assertThat(testLoraGateway.getGatewayId()).isEqualTo(UPDATED_GATEWAY_ID);
        assertThat(testLoraGateway.getName()).isEqualTo(UPDATED_NAME);

        // Validate the LoraGateway in Elasticsearch
        verify(mockLoraGatewaySearchRepository, times(1)).save(testLoraGateway);
    }

    @Test
    @Transactional
    public void updateNonExistingLoraGateway() throws Exception {
        int databaseSizeBeforeUpdate = loraGatewayRepository.findAll().size();

        // Create the LoraGateway
        LoraGatewayDTO loraGatewayDTO = loraGatewayMapper.toDto(loraGateway);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restLoraGatewayMockMvc.perform(put("/api/lora-gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraGatewayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoraGateway in the database
        List<LoraGateway> loraGatewayList = loraGatewayRepository.findAll();
        assertThat(loraGatewayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoraGateway in Elasticsearch
        verify(mockLoraGatewaySearchRepository, times(0)).save(loraGateway);
    }

    @Test
    @Transactional
    public void deleteLoraGateway() throws Exception {
        // Initialize the database
        loraGatewayRepository.saveAndFlush(loraGateway);

        int databaseSizeBeforeDelete = loraGatewayRepository.findAll().size();

        // Get the loraGateway
        restLoraGatewayMockMvc.perform(delete("/api/lora-gateways/{id}", loraGateway.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LoraGateway> loraGatewayList = loraGatewayRepository.findAll();
        assertThat(loraGatewayList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoraGateway in Elasticsearch
        verify(mockLoraGatewaySearchRepository, times(1)).deleteById(loraGateway.getId());
    }

    @Test
    @Transactional
    public void searchLoraGateway() throws Exception {
        // Initialize the database
        loraGatewayRepository.saveAndFlush(loraGateway);
        when(mockLoraGatewaySearchRepository.search(queryStringQuery("id:" + loraGateway.getId())))
            .thenReturn(Collections.singletonList(loraGateway));
        // Search the loraGateway
        restLoraGatewayMockMvc.perform(get("/api/_search/lora-gateways?query=id:" + loraGateway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loraGateway.getId().intValue())))
            .andExpect(jsonPath("$.[*].gatewayId").value(hasItem(DEFAULT_GATEWAY_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoraGateway.class);
        LoraGateway loraGateway1 = new LoraGateway();
        loraGateway1.setId(1L);
        LoraGateway loraGateway2 = new LoraGateway();
        loraGateway2.setId(loraGateway1.getId());
        assertThat(loraGateway1).isEqualTo(loraGateway2);
        loraGateway2.setId(2L);
        assertThat(loraGateway1).isNotEqualTo(loraGateway2);
        loraGateway1.setId(null);
        assertThat(loraGateway1).isNotEqualTo(loraGateway2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoraGatewayDTO.class);
        LoraGatewayDTO loraGatewayDTO1 = new LoraGatewayDTO();
        loraGatewayDTO1.setId(1L);
        LoraGatewayDTO loraGatewayDTO2 = new LoraGatewayDTO();
        assertThat(loraGatewayDTO1).isNotEqualTo(loraGatewayDTO2);
        loraGatewayDTO2.setId(loraGatewayDTO1.getId());
        assertThat(loraGatewayDTO1).isEqualTo(loraGatewayDTO2);
        loraGatewayDTO2.setId(2L);
        assertThat(loraGatewayDTO1).isNotEqualTo(loraGatewayDTO2);
        loraGatewayDTO1.setId(null);
        assertThat(loraGatewayDTO1).isNotEqualTo(loraGatewayDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(loraGatewayMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(loraGatewayMapper.fromId(null)).isNull();
    }
}
