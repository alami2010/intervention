package com.ydahar.jbd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ydahar.jbd.IntegrationTest;
import com.ydahar.jbd.domain.Tier;
import com.ydahar.jbd.repository.TierRepository;
import com.ydahar.jbd.service.dto.TierDTO;
import com.ydahar.jbd.service.mapper.TierMapper;
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
 * Integration tests for the {@link TierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TierRepository tierRepository;

    @Autowired
    private TierMapper tierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTierMockMvc;

    private Tier tier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tier createEntity(EntityManager em) {
        Tier tier = new Tier().name(DEFAULT_NAME);
        return tier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tier createUpdatedEntity(EntityManager em) {
        Tier tier = new Tier().name(UPDATED_NAME);
        return tier;
    }

    @BeforeEach
    public void initTest() {
        tier = createEntity(em);
    }

    @Test
    @Transactional
    void createTier() throws Exception {
        int databaseSizeBeforeCreate = tierRepository.findAll().size();
        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);
        restTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDTO)))
            .andExpect(status().isCreated());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeCreate + 1);
        Tier testTier = tierList.get(tierList.size() - 1);
        assertThat(testTier.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTierWithExistingId() throws Exception {
        // Create the Tier with an existing ID
        tier.setId(1L);
        TierDTO tierDTO = tierMapper.toDto(tier);

        int databaseSizeBeforeCreate = tierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTiers() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        // Get all the tierList
        restTierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTier() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        // Get the tier
        restTierMockMvc
            .perform(get(ENTITY_API_URL_ID, tier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTier() throws Exception {
        // Get the tier
        restTierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTier() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        int databaseSizeBeforeUpdate = tierRepository.findAll().size();

        // Update the tier
        Tier updatedTier = tierRepository.findById(tier.getId()).get();
        // Disconnect from session so that the updates on updatedTier are not directly saved in db
        em.detach(updatedTier);
        updatedTier.name(UPDATED_NAME);
        TierDTO tierDTO = tierMapper.toDto(updatedTier);

        restTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
        Tier testTier = tierList.get(tierList.size() - 1);
        assertThat(testTier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTierWithPatch() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        int databaseSizeBeforeUpdate = tierRepository.findAll().size();

        // Update the tier using partial update
        Tier partialUpdatedTier = new Tier();
        partialUpdatedTier.setId(tier.getId());

        partialUpdatedTier.name(UPDATED_NAME);

        restTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTier))
            )
            .andExpect(status().isOk());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
        Tier testTier = tierList.get(tierList.size() - 1);
        assertThat(testTier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTierWithPatch() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        int databaseSizeBeforeUpdate = tierRepository.findAll().size();

        // Update the tier using partial update
        Tier partialUpdatedTier = new Tier();
        partialUpdatedTier.setId(tier.getId());

        partialUpdatedTier.name(UPDATED_NAME);

        restTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTier))
            )
            .andExpect(status().isOk());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
        Tier testTier = tierList.get(tierList.size() - 1);
        assertThat(testTier.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTier() throws Exception {
        int databaseSizeBeforeUpdate = tierRepository.findAll().size();
        tier.setId(count.incrementAndGet());

        // Create the Tier
        TierDTO tierDTO = tierMapper.toDto(tier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tier in the database
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTier() throws Exception {
        // Initialize the database
        tierRepository.saveAndFlush(tier);

        int databaseSizeBeforeDelete = tierRepository.findAll().size();

        // Delete the tier
        restTierMockMvc
            .perform(delete(ENTITY_API_URL_ID, tier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tier> tierList = tierRepository.findAll();
        assertThat(tierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
