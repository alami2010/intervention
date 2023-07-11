package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FloorMapperTest {

    private FloorMapper floorMapper;

    @BeforeEach
    public void setUp() {
        floorMapper = new FloorMapperImpl();
    }
}
