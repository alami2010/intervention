package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.RoomData;
import com.ydahar.jbd.repository.RoomDataRepository;
import com.ydahar.jbd.service.dto.RoomDataDTO;
import com.ydahar.jbd.service.mapper.RoomDataMapper;
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
 * Integration tests for the {@link RoomDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomDataResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/room-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomDataRepository roomDataRepository;

    @Autowired
    private RoomDataMapper roomDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomDataMockMvc;

    private RoomData roomData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomData createEntity(EntityManager em) {
        RoomData roomData = new RoomData().name(DEFAULT_NAME);
        return roomData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomData createUpdatedEntity(EntityManager em) {
        RoomData roomData = new RoomData().name(UPDATED_NAME);
        return roomData;
    }

    @BeforeEach
    public void initTest() {
        roomData = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomData() throws Exception {
        int databaseSizeBeforeCreate = roomDataRepository.findAll().size();
        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);
        restRoomDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDataDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeCreate + 1);
        RoomData testRoomData = roomDataList.get(roomDataList.size() - 1);
        assertThat(testRoomData.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRoomDataWithExistingId() throws Exception {
        // Create the RoomData with an existing ID
        roomData.setId(1L);
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        int databaseSizeBeforeCreate = roomDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoomData() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        // Get all the roomDataList
        restRoomDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomData.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRoomData() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        // Get the roomData
        restRoomDataMockMvc
            .perform(get(ENTITY_API_URL_ID, roomData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomData.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRoomData() throws Exception {
        // Get the roomData
        restRoomDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomData() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();

        // Update the roomData
        RoomData updatedRoomData = roomDataRepository.findById(roomData.getId()).get();
        // Disconnect from session so that the updates on updatedRoomData are not directly saved in db
        em.detach(updatedRoomData);
        updatedRoomData.name(UPDATED_NAME);
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(updatedRoomData);

        restRoomDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
        RoomData testRoomData = roomDataList.get(roomDataList.size() - 1);
        assertThat(testRoomData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomDataWithPatch() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();

        // Update the roomData using partial update
        RoomData partialUpdatedRoomData = new RoomData();
        partialUpdatedRoomData.setId(roomData.getId());

        restRoomDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomData))
            )
            .andExpect(status().isOk());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
        RoomData testRoomData = roomDataList.get(roomDataList.size() - 1);
        assertThat(testRoomData.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRoomDataWithPatch() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();

        // Update the roomData using partial update
        RoomData partialUpdatedRoomData = new RoomData();
        partialUpdatedRoomData.setId(roomData.getId());

        partialUpdatedRoomData.name(UPDATED_NAME);

        restRoomDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomData))
            )
            .andExpect(status().isOk());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
        RoomData testRoomData = roomDataList.get(roomDataList.size() - 1);
        assertThat(testRoomData.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomData() throws Exception {
        int databaseSizeBeforeUpdate = roomDataRepository.findAll().size();
        roomData.setId(count.incrementAndGet());

        // Create the RoomData
        RoomDataDTO roomDataDTO = roomDataMapper.toDto(roomData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomData in the database
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomData() throws Exception {
        // Initialize the database
        roomDataRepository.saveAndFlush(roomData);

        int databaseSizeBeforeDelete = roomDataRepository.findAll().size();

        // Delete the roomData
        restRoomDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomData> roomDataList = roomDataRepository.findAll();
        assertThat(roomDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
