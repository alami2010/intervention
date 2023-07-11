package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.TierData;
import com.ydahar.jbd.repository.TierDataRepository;
import com.ydahar.jbd.service.dto.TierDataDTO;
import com.ydahar.jbd.service.mapper.TierDataMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TierDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TierDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tier-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TierDataRepository tierDataRepository;

    @Autowired
    private TierDataMapper tierDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTierDataMockMvc;

    private TierData tierData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TierData createEntity(EntityManager em) {
        TierData tierData = new TierData().name(DEFAULT_NAME);
        return tierData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TierData createUpdatedEntity(EntityManager em) {
        TierData tierData = new TierData().name(UPDATED_NAME);
        return tierData;
    }

    @BeforeEach
    public void initTest() {
        tierData = createEntity(em);
    }

    @Test
    @Transactional
    void createTierData() throws Exception {
        int databaseSizeBeforeCreate = tierDataRepository.findAll().size();
        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);
        restTierDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDataDTO)))
            .andExpect(status().isCreated());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeCreate + 1);
        TierData testTierData = tierDataList.get(tierDataList.size() - 1);
        assertThat(testTierData.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTierDataWithExistingId() throws Exception {
        // Create the TierData with an existing ID
        tierData.setId(1L);
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        int databaseSizeBeforeCreate = tierDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTierDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTierData() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        // Get all the tierDataList
        restTierDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tierData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTierData() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        // Get the tierData
        restTierDataMockMvc
            .perform(get(ENTITY_API_URL_ID, tierData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tierData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTierData() throws Exception {
        // Get the tierData
        restTierDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTierData() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();

        // Update the tierData
        TierData updatedTierData = tierDataRepository.findById(tierData.getId()).get();
        // Disconnect from session so that the updates on updatedTierData are not directly saved in db
        em.detach(updatedTierData);
        updatedTierData.name(UPDATED_NAME);
        TierDataDTO tierDataDTO = tierDataMapper.toDto(updatedTierData);

        restTierDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tierDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
        TierData testTierData = tierDataList.get(tierDataList.size() - 1);
        assertThat(testTierData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tierDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTierDataWithPatch() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();

        // Update the tierData using partial update
        TierData partialUpdatedTierData = new TierData();
        partialUpdatedTierData.setId(tierData.getId());

        restTierDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTierData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTierData))
            )
            .andExpect(status().isOk());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
        TierData testTierData = tierDataList.get(tierDataList.size() - 1);
        assertThat(testTierData.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTierDataWithPatch() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();

        // Update the tierData using partial update
        TierData partialUpdatedTierData = new TierData();
        partialUpdatedTierData.setId(tierData.getId());

        partialUpdatedTierData.name(UPDATED_NAME);

        restTierDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTierData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTierData))
            )
            .andExpect(status().isOk());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
        TierData testTierData = tierDataList.get(tierDataList.size() - 1);
        assertThat(testTierData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tierDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTierData() throws Exception {
        int databaseSizeBeforeUpdate = tierDataRepository.findAll().size();
        tierData.setId(count.incrementAndGet());

        // Create the TierData
        TierDataDTO tierDataDTO = tierDataMapper.toDto(tierData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tierDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TierData in the database
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTierData() throws Exception {
        // Initialize the database
        tierDataRepository.saveAndFlush(tierData);

        int databaseSizeBeforeDelete = tierDataRepository.findAll().size();

        // Delete the tierData
        restTierDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, tierData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TierData> tierDataList = tierDataRepository.findAll();
        assertThat(tierDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
