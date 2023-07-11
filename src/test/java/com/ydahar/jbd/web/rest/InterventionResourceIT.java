package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.Intervention;
import com.ydahar.jbd.domain.enumeration.TypeIntervention;
import com.ydahar.jbd.repository.InterventionRepository;
import com.ydahar.jbd.service.dto.InterventionDTO;
import com.ydahar.jbd.service.mapper.InterventionMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link InterventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterventionResourceIT {

    private static final TypeIntervention DEFAULT_TYPE = TypeIntervention.Construction;
    private static final TypeIntervention UPDATED_TYPE = TypeIntervention.Emergency;

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINISH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RAISON = "AAAAAAAAAA";
    private static final String UPDATED_RAISON = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/interventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionMapper interventionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .type(DEFAULT_TYPE)
            .start(DEFAULT_START)
            .finish(DEFAULT_FINISH)
            .raison(DEFAULT_RAISON)
            .unitNumber(DEFAULT_UNIT_NUMBER)
            .creationDate(DEFAULT_CREATION_DATE);
        return intervention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createUpdatedEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .type(UPDATED_TYPE)
            .start(UPDATED_START)
            .finish(UPDATED_FINISH)
            .raison(UPDATED_RAISON)
            .unitNumber(UPDATED_UNIT_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);
        return intervention;
    }

    @BeforeEach
    public void initTest() {
        intervention = createEntity(em);
    }

    @Test
    @Transactional
    void createIntervention() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();
        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);
        restInterventionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIntervention.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testIntervention.getFinish()).isEqualTo(DEFAULT_FINISH);
        assertThat(testIntervention.getRaison()).isEqualTo(DEFAULT_RAISON);
        assertThat(testIntervention.getUnitNumber()).isEqualTo(DEFAULT_UNIT_NUMBER);
        assertThat(testIntervention.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void createInterventionWithExistingId() throws Exception {
        // Create the Intervention with an existing ID
        intervention.setId(1L);
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInterventions() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].finish").value(hasItem(DEFAULT_FINISH.toString())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON)))
            .andExpect(jsonPath("$.[*].unitNumber").value(hasItem(DEFAULT_UNIT_NUMBER)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL_ID, intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.finish").value(DEFAULT_FINISH.toString()))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON))
            .andExpect(jsonPath("$.unitNumber").value(DEFAULT_UNIT_NUMBER))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findById(intervention.getId()).get();
        // Disconnect from session so that the updates on updatedIntervention are not directly saved in db
        em.detach(updatedIntervention);
        updatedIntervention
            .type(UPDATED_TYPE)
            .start(UPDATED_START)
            .finish(UPDATED_FINISH)
            .raison(UPDATED_RAISON)
            .unitNumber(UPDATED_UNIT_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);
        InterventionDTO interventionDTO = interventionMapper.toDto(updatedIntervention);

        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntervention.getStart()).isEqualTo(UPDATED_START);
        assertThat(testIntervention.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testIntervention.getRaison()).isEqualTo(UPDATED_RAISON);
        assertThat(testIntervention.getUnitNumber()).isEqualTo(UPDATED_UNIT_NUMBER);
        assertThat(testIntervention.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .type(UPDATED_TYPE)
            .finish(UPDATED_FINISH)
            .raison(UPDATED_RAISON)
            .unitNumber(UPDATED_UNIT_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntervention.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testIntervention.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testIntervention.getRaison()).isEqualTo(UPDATED_RAISON);
        assertThat(testIntervention.getUnitNumber()).isEqualTo(UPDATED_UNIT_NUMBER);
        assertThat(testIntervention.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .type(UPDATED_TYPE)
            .start(UPDATED_START)
            .finish(UPDATED_FINISH)
            .raison(UPDATED_RAISON)
            .unitNumber(UPDATED_UNIT_NUMBER)
            .creationDate(UPDATED_CREATION_DATE);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntervention.getStart()).isEqualTo(UPDATED_START);
        assertThat(testIntervention.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testIntervention.getRaison()).isEqualTo(UPDATED_RAISON);
        assertThat(testIntervention.getUnitNumber()).isEqualTo(UPDATED_UNIT_NUMBER);
        assertThat(testIntervention.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(count.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interventionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeDelete = interventionRepository.findAll().size();

        // Delete the intervention
        restInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
