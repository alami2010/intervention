package com.ydahar.jbd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloorDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloorData.class);
        FloorData floorData1 = new FloorData();
        floorData1.setId(1L);
        FloorData floorData2 = new FloorData();
        floorData2.setId(floorData1.getId());
        assertThat(floorData1).isEqualTo(floorData2);
        floorData2.setId(2L);
        assertThat(floorData1).isNotEqualTo(floorData2);
        floorData1.setId(null);
        assertThat(floorData1).isNotEqualTo(floorData2);
    }
}
