package com.mpiatrenka.aviationwrapper;

import com.mpiatrenka.aviationwrapper.client.AirportsClient;
import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class SmokeIntegrationTest {

    @Container
    static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName
            .parse("redis:7.2"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
    }

    @Autowired
    MockMvc mvc;

    @MockitoBean
    AirportsClient airportsClient;

    @Test
    void smoke_getAirportDetails_returns200_andCachesInRedis() throws Exception {
        String icaoId = "KJFK";

        AirportDetails details = new AirportDetails();
        details.setIcaoId(icaoId);
        details.setName("John F. Kennedy Intl");
        List<AirportDetails> airportDetails = new ArrayList<>();
        airportDetails.add(details);

        when(airportsClient.getAirportsDetails(icaoId)).thenReturn(airportDetails);

        // 1st call -> should hit Feign client and store in Redis
        mvc.perform(get("/api/v1//airports/{icaoId}", icaoId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].icaoId").value(icaoId))
                .andExpect(jsonPath("$[0].name").value("John F. Kennedy Intl"));

        // 2nd call -> should come from cache (client called only once)
        mvc.perform(get("/api/v1/airports/{icaoId}", icaoId)).andExpect(status().isOk());

        verify(airportsClient, times(1)).getAirportsDetails(icaoId);
        verifyNoMoreInteractions(airportsClient);
    }
}
