package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TierDataMapperTest {

    private TierDataMapper tierDataMapper;

    @BeforeEach
    public void setUp() {
        tierDataMapper = new TierDataMapperImpl();
    }
}
