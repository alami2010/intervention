package com.ydahar.jbd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TierDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TierDataDTO.class);
        TierDataDTO tierDataDTO1 = new TierDataDTO();
        tierDataDTO1.setId(1L);
        TierDataDTO tierDataDTO2 = new TierDataDTO();
        assertThat(tierDataDTO1).isNotEqualTo(tierDataDTO2);
        tierDataDTO2.setId(tierDataDTO1.getId());
        assertThat(tierDataDTO1).isEqualTo(tierDataDTO2);
        tierDataDTO2.setId(2L);
        assertThat(tierDataDTO1).isNotEqualTo(tierDataDTO2);
        tierDataDTO1.setId(null);
        assertThat(tierDataDTO1).isNotEqualTo(tierDataDTO2);
    }
}
