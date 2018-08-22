package com.sensocon.core.web.rest;

import com.sensocon.core.SensoconServerApp;

import com.sensocon.core.domain.LoraPacket;
import com.sensocon.core.repository.LoraPacketRepository;
import com.sensocon.core.repository.search.LoraPacketSearchRepository;
import com.sensocon.core.service.LoraPacketService;
import com.sensocon.core.service.dto.LoraPacketDTO;
import com.sensocon.core.service.mapper.LoraPacketMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the LoraPacketResource REST controller.
 *
 * @see LoraPacketResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SensoconServerApp.class)
public class LoraPacketResourceIntTest {

    private static final Double DEFAULT_RSSI = 1D;
    private static final Double UPDATED_RSSI = 2D;

    private static final Double DEFAULT_BATTERY_LEVEL = 1D;
    private static final Double UPDATED_BATTERY_LEVEL = 2D;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_TEMPERATURE = 1D;
    private static final Double UPDATED_TEMPERATURE = 2D;

    private static final Double DEFAULT_PRESSURE = 1D;
    private static final Double UPDATED_PRESSURE = 2D;

    @Autowired
    private LoraPacketRepository loraPacketRepository;


    @Autowired
    private LoraPacketMapper loraPacketMapper;
    

    @Autowired
    private LoraPacketService loraPacketService;

    /**
     * This repository is mocked in the com.sensocon.core.repository.search test package.
     *
     * @see com.sensocon.core.repository.search.LoraPacketSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoraPacketSearchRepository mockLoraPacketSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLoraPacketMockMvc;

    private LoraPacket loraPacket;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoraPacketResource loraPacketResource = new LoraPacketResource(loraPacketService);
        this.restLoraPacketMockMvc = MockMvcBuilders.standaloneSetup(loraPacketResource)
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
    public static LoraPacket createEntity(EntityManager em) {
        LoraPacket loraPacket = new LoraPacket()
            .rssi(DEFAULT_RSSI)
            .batteryLevel(DEFAULT_BATTERY_LEVEL)
            .timestamp(DEFAULT_TIMESTAMP)
            .temperature(DEFAULT_TEMPERATURE)
            .pressure(DEFAULT_PRESSURE);
        return loraPacket;
    }

    @Before
    public void initTest() {
        loraPacket = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoraPacket() throws Exception {
        int databaseSizeBeforeCreate = loraPacketRepository.findAll().size();

        // Create the LoraPacket
        LoraPacketDTO loraPacketDTO = loraPacketMapper.toDto(loraPacket);
        restLoraPacketMockMvc.perform(post("/api/lora-packets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraPacketDTO)))
            .andExpect(status().isCreated());

        // Validate the LoraPacket in the database
        List<LoraPacket> loraPacketList = loraPacketRepository.findAll();
        assertThat(loraPacketList).hasSize(databaseSizeBeforeCreate + 1);
        LoraPacket testLoraPacket = loraPacketList.get(loraPacketList.size() - 1);
        assertThat(testLoraPacket.getRssi()).isEqualTo(DEFAULT_RSSI);
        assertThat(testLoraPacket.getBatteryLevel()).isEqualTo(DEFAULT_BATTERY_LEVEL);
        assertThat(testLoraPacket.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testLoraPacket.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testLoraPacket.getPressure()).isEqualTo(DEFAULT_PRESSURE);

        // Validate the LoraPacket in Elasticsearch
        verify(mockLoraPacketSearchRepository, times(1)).save(testLoraPacket);
    }

    @Test
    @Transactional
    public void createLoraPacketWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loraPacketRepository.findAll().size();

        // Create the LoraPacket with an existing ID
        loraPacket.setId(1L);
        LoraPacketDTO loraPacketDTO = loraPacketMapper.toDto(loraPacket);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoraPacketMockMvc.perform(post("/api/lora-packets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraPacketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoraPacket in the database
        List<LoraPacket> loraPacketList = loraPacketRepository.findAll();
        assertThat(loraPacketList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoraPacket in Elasticsearch
        verify(mockLoraPacketSearchRepository, times(0)).save(loraPacket);
    }

    @Test
    @Transactional
    public void getAllLoraPackets() throws Exception {
        // Initialize the database
        loraPacketRepository.saveAndFlush(loraPacket);

        // Get all the loraPacketList
        restLoraPacketMockMvc.perform(get("/api/lora-packets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loraPacket.getId().intValue())))
            .andExpect(jsonPath("$.[*].rssi").value(hasItem(DEFAULT_RSSI.doubleValue())))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].pressure").value(hasItem(DEFAULT_PRESSURE.doubleValue())));
    }
    

    @Test
    @Transactional
    public void getLoraPacket() throws Exception {
        // Initialize the database
        loraPacketRepository.saveAndFlush(loraPacket);

        // Get the loraPacket
        restLoraPacketMockMvc.perform(get("/api/lora-packets/{id}", loraPacket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loraPacket.getId().intValue()))
            .andExpect(jsonPath("$.rssi").value(DEFAULT_RSSI.doubleValue()))
            .andExpect(jsonPath("$.batteryLevel").value(DEFAULT_BATTERY_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.pressure").value(DEFAULT_PRESSURE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingLoraPacket() throws Exception {
        // Get the loraPacket
        restLoraPacketMockMvc.perform(get("/api/lora-packets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoraPacket() throws Exception {
        // Initialize the database
        loraPacketRepository.saveAndFlush(loraPacket);

        int databaseSizeBeforeUpdate = loraPacketRepository.findAll().size();

        // Update the loraPacket
        LoraPacket updatedLoraPacket = loraPacketRepository.findById(loraPacket.getId()).get();
        // Disconnect from session so that the updates on updatedLoraPacket are not directly saved in db
        em.detach(updatedLoraPacket);
        updatedLoraPacket
            .rssi(UPDATED_RSSI)
            .batteryLevel(UPDATED_BATTERY_LEVEL)
            .timestamp(UPDATED_TIMESTAMP)
            .temperature(UPDATED_TEMPERATURE)
            .pressure(UPDATED_PRESSURE);
        LoraPacketDTO loraPacketDTO = loraPacketMapper.toDto(updatedLoraPacket);

        restLoraPacketMockMvc.perform(put("/api/lora-packets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraPacketDTO)))
            .andExpect(status().isOk());

        // Validate the LoraPacket in the database
        List<LoraPacket> loraPacketList = loraPacketRepository.findAll();
        assertThat(loraPacketList).hasSize(databaseSizeBeforeUpdate);
        LoraPacket testLoraPacket = loraPacketList.get(loraPacketList.size() - 1);
        assertThat(testLoraPacket.getRssi()).isEqualTo(UPDATED_RSSI);
        assertThat(testLoraPacket.getBatteryLevel()).isEqualTo(UPDATED_BATTERY_LEVEL);
        assertThat(testLoraPacket.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testLoraPacket.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testLoraPacket.getPressure()).isEqualTo(UPDATED_PRESSURE);

        // Validate the LoraPacket in Elasticsearch
        verify(mockLoraPacketSearchRepository, times(1)).save(testLoraPacket);
    }

    @Test
    @Transactional
    public void updateNonExistingLoraPacket() throws Exception {
        int databaseSizeBeforeUpdate = loraPacketRepository.findAll().size();

        // Create the LoraPacket
        LoraPacketDTO loraPacketDTO = loraPacketMapper.toDto(loraPacket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restLoraPacketMockMvc.perform(put("/api/lora-packets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loraPacketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoraPacket in the database
        List<LoraPacket> loraPacketList = loraPacketRepository.findAll();
        assertThat(loraPacketList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoraPacket in Elasticsearch
        verify(mockLoraPacketSearchRepository, times(0)).save(loraPacket);
    }

    @Test
    @Transactional
    public void deleteLoraPacket() throws Exception {
        // Initialize the database
        loraPacketRepository.saveAndFlush(loraPacket);

        int databaseSizeBeforeDelete = loraPacketRepository.findAll().size();

        // Get the loraPacket
        restLoraPacketMockMvc.perform(delete("/api/lora-packets/{id}", loraPacket.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LoraPacket> loraPacketList = loraPacketRepository.findAll();
        assertThat(loraPacketList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoraPacket in Elasticsearch
        verify(mockLoraPacketSearchRepository, times(1)).deleteById(loraPacket.getId());
    }

    @Test
    @Transactional
    public void searchLoraPacket() throws Exception {
        // Initialize the database
        loraPacketRepository.saveAndFlush(loraPacket);
        when(mockLoraPacketSearchRepository.search(queryStringQuery("id:" + loraPacket.getId())))
            .thenReturn(Collections.singletonList(loraPacket));
        // Search the loraPacket
        restLoraPacketMockMvc.perform(get("/api/_search/lora-packets?query=id:" + loraPacket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loraPacket.getId().intValue())))
            .andExpect(jsonPath("$.[*].rssi").value(hasItem(DEFAULT_RSSI.doubleValue())))
            .andExpect(jsonPath("$.[*].batteryLevel").value(hasItem(DEFAULT_BATTERY_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].pressure").value(hasItem(DEFAULT_PRESSURE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoraPacket.class);
        LoraPacket loraPacket1 = new LoraPacket();
        loraPacket1.setId(1L);
        LoraPacket loraPacket2 = new LoraPacket();
        loraPacket2.setId(loraPacket1.getId());
        assertThat(loraPacket1).isEqualTo(loraPacket2);
        loraPacket2.setId(2L);
        assertThat(loraPacket1).isNotEqualTo(loraPacket2);
        loraPacket1.setId(null);
        assertThat(loraPacket1).isNotEqualTo(loraPacket2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoraPacketDTO.class);
        LoraPacketDTO loraPacketDTO1 = new LoraPacketDTO();
        loraPacketDTO1.setId(1L);
        LoraPacketDTO loraPacketDTO2 = new LoraPacketDTO();
        assertThat(loraPacketDTO1).isNotEqualTo(loraPacketDTO2);
        loraPacketDTO2.setId(loraPacketDTO1.getId());
        assertThat(loraPacketDTO1).isEqualTo(loraPacketDTO2);
        loraPacketDTO2.setId(2L);
        assertThat(loraPacketDTO1).isNotEqualTo(loraPacketDTO2);
        loraPacketDTO1.setId(null);
        assertThat(loraPacketDTO1).isNotEqualTo(loraPacketDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(loraPacketMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(loraPacketMapper.fromId(null)).isNull();
    }
}
