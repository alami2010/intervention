package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.FloorData;
import com.ydahar.jbd.repository.FloorDataRepository;
import com.ydahar.jbd.service.dto.FloorDataDTO;
import com.ydahar.jbd.service.mapper.FloorDataMapper;
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
 * Integration tests for the {@link FloorDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FloorDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/floor-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FloorDataRepository floorDataRepository;

    @Autowired
    private FloorDataMapper floorDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFloorDataMockMvc;

    private FloorData floorData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloorData createEntity(EntityManager em) {
        FloorData floorData = new FloorData().name(DEFAULT_NAME);
        return floorData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FloorData createUpdatedEntity(EntityManager em) {
        FloorData floorData = new FloorData().name(UPDATED_NAME);
        return floorData;
    }

    @BeforeEach
    public void initTest() {
        floorData = createEntity(em);
    }

    @Test
    @Transactional
    void createFloorData() throws Exception {
        int databaseSizeBeforeCreate = floorDataRepository.findAll().size();
        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);
        restFloorDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDataDTO)))
            .andExpect(status().isCreated());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeCreate + 1);
        FloorData testFloorData = floorDataList.get(floorDataList.size() - 1);
        assertThat(testFloorData.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFloorDataWithExistingId() throws Exception {
        // Create the FloorData with an existing ID
        floorData.setId(1L);
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        int databaseSizeBeforeCreate = floorDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFloorDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFloorData() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        // Get all the floorDataList
        restFloorDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(floorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFloorData() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        // Get the floorData
        restFloorDataMockMvc
            .perform(get(ENTITY_API_URL_ID, floorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(floorData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFloorData() throws Exception {
        // Get the floorData
        restFloorDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFloorData() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();

        // Update the floorData
        FloorData updatedFloorData = floorDataRepository.findById(floorData.getId()).get();
        // Disconnect from session so that the updates on updatedFloorData are not directly saved in db
        em.detach(updatedFloorData);
        updatedFloorData.name(UPDATED_NAME);
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(updatedFloorData);

        restFloorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
        FloorData testFloorData = floorDataList.get(floorDataList.size() - 1);
        assertThat(testFloorData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFloorDataWithPatch() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();

        // Update the floorData using partial update
        FloorData partialUpdatedFloorData = new FloorData();
        partialUpdatedFloorData.setId(floorData.getId());

        partialUpdatedFloorData.name(UPDATED_NAME);

        restFloorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloorData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloorData))
            )
            .andExpect(status().isOk());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
        FloorData testFloorData = floorDataList.get(floorDataList.size() - 1);
        assertThat(testFloorData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFloorDataWithPatch() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();

        // Update the floorData using partial update
        FloorData partialUpdatedFloorData = new FloorData();
        partialUpdatedFloorData.setId(floorData.getId());

        partialUpdatedFloorData.name(UPDATED_NAME);

        restFloorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloorData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloorData))
            )
            .andExpect(status().isOk());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
        FloorData testFloorData = floorDataList.get(floorDataList.size() - 1);
        assertThat(testFloorData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, floorDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFloorData() throws Exception {
        int databaseSizeBeforeUpdate = floorDataRepository.findAll().size();
        floorData.setId(count.incrementAndGet());

        // Create the FloorData
        FloorDataDTO floorDataDTO = floorDataMapper.toDto(floorData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(floorDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FloorData in the database
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFloorData() throws Exception {
        // Initialize the database
        floorDataRepository.saveAndFlush(floorData);

        int databaseSizeBeforeDelete = floorDataRepository.findAll().size();

        // Delete the floorData
        restFloorDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, floorData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FloorData> floorDataList = floorDataRepository.findAll();
        assertThat(floorDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
