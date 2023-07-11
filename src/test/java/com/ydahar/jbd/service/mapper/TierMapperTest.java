package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TierMapperTest {

    private TierMapper tierMapper;

    @BeforeEach
    public void setUp() {
        tierMapper = new TierMapperImpl();
    }
}
