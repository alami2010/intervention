package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FloorDataMapperTest {

    private FloorDataMapper floorDataMapper;

    @BeforeEach
    public void setUp() {
        floorDataMapper = new FloorDataMapperImpl();
    }
}
