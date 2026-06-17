package com.mpiatrenka.aviationwrapper.service;

import com.mpiatrenka.aviationwrapper.cache.AirportDetailsCache;
import com.mpiatrenka.aviationwrapper.client.AirportsClient;
import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportsServiceImplTest {

    @InjectMocks
    private AirportsServiceImpl airportsService;

    @Mock
    private AirportsClient airportsClient;
    @Mock
    private AirportDetailsCache  airportDetailsCache;
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @Test
    void getAirportDetails_returnsAirportDetails_notPresentInCache() {
        AirportDetails airportDetail = new AirportDetails();
        airportDetail.setIcaoId("KJFK");
        var airportDetails = List.of(airportDetail);
        when(airportDetailsCache.getAirportDetails(anyString()))
                .thenReturn(Optional.empty());
        when(airportsClient.getAirportsDetails(anyString()))
                .thenReturn(airportDetails);

        var result = airportsService.getAirportDetails("KJFK");

        assertNotNull(result);
        assertNotNull(result.getFirst());
        assertEquals("KJFK", result.getFirst().getIcaoId());
        verify(airportDetailsCache).getAirportDetails("KJFK");
        verify(airportsClient).getAirportsDetails("KJFK");
        verify(airportDetailsCache).saveDetails("KJFK", airportDetails);
    }

    @Test
    void getAirportDetails_returnsAirportDetails_presentInCache() {
        AirportDetails airportDetail = new AirportDetails();
        airportDetail.setIcaoId("KJFK");
        var airportDetails = List.of(airportDetail);
        when(airportDetailsCache.getAirportDetails(anyString()))
                .thenReturn(Optional.of(airportDetails));

        var result = airportsService.getAirportDetails("KJFK");

        assertNotNull(result);
        assertNotNull(result.getFirst());
        assertEquals("KJFK", result.getFirst().getIcaoId());
        verify(airportDetailsCache).getAirportDetails("KJFK");
        verifyNoInteractions(airportsClient);
        verifyNoMoreInteractions(airportDetailsCache);
    }


    @ParameterizedTest
    @MethodSource("airportDetails")
    void getAirportDetails_noDetailsIsReturned(List<AirportDetails> details) {
        var icaoId = "KJFK";
        when(airportDetailsCache.getAirportDetails(icaoId))
                .thenReturn(Optional.empty());
        when(airportsClient.getAirportsDetails(icaoId))
                .thenReturn(details);
        var result = airportsService.getAirportDetails(icaoId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verifyNoMoreInteractions(airportDetailsCache);
    }


    private static Stream<Arguments> airportDetails() {
        return Stream.of(
                Arguments.of(List.of())
        );
    }
}