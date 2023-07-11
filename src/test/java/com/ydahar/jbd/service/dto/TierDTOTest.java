package com.ydahar.jbd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TierDTO.class);
        TierDTO tierDTO1 = new TierDTO();
        tierDTO1.setId(1L);
        TierDTO tierDTO2 = new TierDTO();
        assertThat(tierDTO1).isNotEqualTo(tierDTO2);
        tierDTO2.setId(tierDTO1.getId());
        assertThat(tierDTO1).isEqualTo(tierDTO2);
        tierDTO2.setId(2L);
        assertThat(tierDTO1).isNotEqualTo(tierDTO2);
        tierDTO1.setId(null);
        assertThat(tierDTO1).isNotEqualTo(tierDTO2);
    }
}
