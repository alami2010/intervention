package com.ydahar.jbd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomData.class);
        RoomData roomData1 = new RoomData();
        roomData1.setId(1L);
        RoomData roomData2 = new RoomData();
        roomData2.setId(roomData1.getId());
        assertThat(roomData1).isEqualTo(roomData2);
        roomData2.setId(2L);
        assertThat(roomData1).isNotEqualTo(roomData2);
        roomData1.setId(null);
        assertThat(roomData1).isNotEqualTo(roomData2);
    }
}
