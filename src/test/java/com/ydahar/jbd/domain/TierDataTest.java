package com.ydahar.jbd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TierDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TierData.class);
        TierData tierData1 = new TierData();
        tierData1.setId(1L);
        TierData tierData2 = new TierData();
        tierData2.setId(tierData1.getId());
        assertThat(tierData1).isEqualTo(tierData2);
        tierData2.setId(2L);
        assertThat(tierData1).isNotEqualTo(tierData2);
        tierData1.setId(null);
        assertThat(tierData1).isNotEqualTo(tierData2);
    }
}
