package com.ydahar.jbd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterventionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterventionDTO.class);
        InterventionDTO interventionDTO1 = new InterventionDTO();
        interventionDTO1.setId(1L);
        InterventionDTO interventionDTO2 = new InterventionDTO();
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO2.setId(interventionDTO1.getId());
        assertThat(interventionDTO1).isEqualTo(interventionDTO2);
        interventionDTO2.setId(2L);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO1.setId(null);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
    }
}
