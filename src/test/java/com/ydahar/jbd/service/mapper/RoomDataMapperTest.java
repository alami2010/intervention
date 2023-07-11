package com.ydahar.jbd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomDataMapperTest {

    private RoomDataMapper roomDataMapper;

    @BeforeEach
    public void setUp() {
        roomDataMapper = new RoomDataMapperImpl();
    }
}
