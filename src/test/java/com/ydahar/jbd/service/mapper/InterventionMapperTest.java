package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterventionMapperTest {

    private InterventionMapper interventionMapper;

    @BeforeEach
    public void setUp() {
        interventionMapper = new InterventionMapperImpl();
    }
}
