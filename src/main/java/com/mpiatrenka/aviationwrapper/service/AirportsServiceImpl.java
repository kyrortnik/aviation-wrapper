package com.mpiatrenka.aviationwrapper.service;

import com.mpiatrenka.aviationwrapper.cache.AirportDetailsCache;
import com.mpiatrenka.aviationwrapper.client.AirportsClient;
import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.annotation.Observed;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportsServiceImpl implements AirportsService {

    private static final String CACHE_LOOKUPS_METRIC = "airport.cache.lookups";

    private final AirportsClient airportsClient;
    private final AirportDetailsCache airportDetailsCache;
    private final MeterRegistry meterRegistry;

    @Observed(name = "airport.details.lookup", contextualName = "get-airport-details")
    @Override
    public List<AirportDetails> getAirportDetails(@NonNull String icaoId) {
        Optional<List<AirportDetails>> airportDetailsOpt = airportDetailsCache.getAirportDetails(icaoId);
        if (airportDetailsOpt.isPresent()) {
            meterRegistry.counter(CACHE_LOOKUPS_METRIC, "result", "hit").increment();
            return airportDetailsOpt.get();
        } else {
            meterRegistry.counter(CACHE_LOOKUPS_METRIC, "result", "miss").increment();
            log.info("Airport details not found in cache for ICAO ID: {}", icaoId);
            List<AirportDetails> airportDetails = airportsClient.getAirportsDetails(icaoId);
            if (airportDetails == null || airportDetails.isEmpty()) {
                return List.of();
            }
            airportDetailsCache.saveDetails(icaoId, airportDetails);
            return airportDetails;
        }

    }
}
