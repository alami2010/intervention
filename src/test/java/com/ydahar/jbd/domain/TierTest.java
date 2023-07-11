package com.ydahar.jbd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ydahar.jbd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tier.class);
        Tier tier1 = new Tier();
        tier1.setId(1L);
        Tier tier2 = new Tier();
        tier2.setId(tier1.getId());
        assertThat(tier1).isEqualTo(tier2);
        tier2.setId(2L);
        assertThat(tier1).isNotEqualTo(tier2);
        tier1.setId(null);
        assertThat(tier1).isNotEqualTo(tier2);
    }
}
