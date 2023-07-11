package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.Floor;
import com.ydahar.jbd.repository.FloorRepository;
import com.ydahar.jbd.service.dto.FloorDTO;
import com.ydahar.jbd.service.mapper.FloorMapper;
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
 * Integration tests for the {@link FloorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FloorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/floors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private FloorMapper floorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFloorMockMvc;

    private Floor floor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Floor createEntity(EntityManager em) {
        Floor floor = new Floor().name(DEFAULT_NAME);
        return floor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Floor createUpdatedEntity(EntityManager em) {
        Floor floor = new Floor().name(UPDATED_NAME);
        return floor;
    }

    @BeforeEach
    public void initTest() {
        floor = createEntity(em);
    }

    @Test
    @Transactional
    void createFloor() throws Exception {
        int databaseSizeBeforeCreate = floorRepository.findAll().size();
        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);
        restFloorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isCreated());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeCreate + 1);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createFloorWithExistingId() throws Exception {
        // Create the Floor with an existing ID
        floor.setId(1L);
        FloorDTO floorDTO = floorMapper.toDto(floor);

        int databaseSizeBeforeCreate = floorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFloorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFloors() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get all the floorList
        restFloorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(floor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        // Get the floor
        restFloorMockMvc
            .perform(get(ENTITY_API_URL_ID, floor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(floor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFloor() throws Exception {
        // Get the floor
        restFloorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor
        Floor updatedFloor = floorRepository.findById(floor.getId()).get();
        // Disconnect from session so that the updates on updatedFloor are not directly saved in db
        em.detach(updatedFloor);
        updatedFloor.name(UPDATED_NAME);
        FloorDTO floorDTO = floorMapper.toDto(updatedFloor);

        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFloorWithPatch() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor using partial update
        Floor partialUpdatedFloor = new Floor();
        partialUpdatedFloor.setId(floor.getId());

        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloor))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFloorWithPatch() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeUpdate = floorRepository.findAll().size();

        // Update the floor using partial update
        Floor partialUpdatedFloor = new Floor();
        partialUpdatedFloor.setId(floor.getId());

        partialUpdatedFloor.name(UPDATED_NAME);

        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFloor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFloor))
            )
            .andExpect(status().isOk());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
        Floor testFloor = floorList.get(floorList.size() - 1);
        assertThat(testFloor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, floorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(floorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFloor() throws Exception {
        int databaseSizeBeforeUpdate = floorRepository.findAll().size();
        floor.setId(count.incrementAndGet());

        // Create the Floor
        FloorDTO floorDTO = floorMapper.toDto(floor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFloorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(floorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Floor in the database
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFloor() throws Exception {
        // Initialize the database
        floorRepository.saveAndFlush(floor);

        int databaseSizeBeforeDelete = floorRepository.findAll().size();

        // Delete the floor
        restFloorMockMvc
            .perform(delete(ENTITY_API_URL_ID, floor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Floor> floorList = floorRepository.findAll();
        assertThat(floorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
