package com.mpiatrenka.aviationwrapper.service;

import com.mpiatrenka.aviationwrapper.cache.AirportDetailsCache;
import com.mpiatrenka.aviationwrapper.client.AirportsClient;
import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
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

    private final AirportsClient airportsClient;
    private final AirportDetailsCache airportDetailsCache;

    @Override
    public List<AirportDetails> getAirportDetails(@NonNull String icaoId) {
        Optional<List<AirportDetails>> airportDetailsOpt = airportDetailsCache.getAirportDetails(icaoId);
        if (airportDetailsOpt.isPresent()) {
            return airportDetailsOpt.get();
        } else {
            log.info("Airport details not found in cache for ICAO ID: {}", icaoId);
            List<AirportDetails> airportDetails = airportsClient.getAirportsDetails(icaoId);
            airportDetailsCache.saveDetails(icaoId, airportDetails);
            return airportDetails;
        }

    }
}
