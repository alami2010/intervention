package com.ydahar.jbd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomDataDTO.class);
        RoomDataDTO roomDataDTO1 = new RoomDataDTO();
        roomDataDTO1.setId(1L);
        RoomDataDTO roomDataDTO2 = new RoomDataDTO();
        assertThat(roomDataDTO1).isNotEqualTo(roomDataDTO2);
        roomDataDTO2.setId(roomDataDTO1.getId());
        assertThat(roomDataDTO1).isEqualTo(roomDataDTO2);
        roomDataDTO2.setId(2L);
        assertThat(roomDataDTO1).isNotEqualTo(roomDataDTO2);
        roomDataDTO1.setId(null);
        assertThat(roomDataDTO1).isNotEqualTo(roomDataDTO2);
    }
}
