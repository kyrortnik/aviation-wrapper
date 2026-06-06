package com.mpiatrenka.aviationwrapper.controller;

import com.mpiatrenka.aviationwrapper.service.AirportsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportsController.class)
class AirportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AirportsService airportsService;

    @Test
    void testGetAirports_200() throws Exception {
        mockMvc.perform(get("/api/v1/airports/KJFK"))
                .andExpect(status().isOk());
    }
}