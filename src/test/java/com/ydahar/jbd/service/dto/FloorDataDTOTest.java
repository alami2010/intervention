package com.ydahar.jbd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloorDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloorDataDTO.class);
        FloorDataDTO floorDataDTO1 = new FloorDataDTO();
        floorDataDTO1.setId(1L);
        FloorDataDTO floorDataDTO2 = new FloorDataDTO();
        assertThat(floorDataDTO1).isNotEqualTo(floorDataDTO2);
        floorDataDTO2.setId(floorDataDTO1.getId());
        assertThat(floorDataDTO1).isEqualTo(floorDataDTO2);
        floorDataDTO2.setId(2L);
        assertThat(floorDataDTO1).isNotEqualTo(floorDataDTO2);
        floorDataDTO1.setId(null);
        assertThat(floorDataDTO1).isNotEqualTo(floorDataDTO2);
    }
}
