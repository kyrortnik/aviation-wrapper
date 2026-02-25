package com.mpiatrenka.aviationwrapper.client;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import com.mpiatrenka.aviationwrapper.fallback.AirportsDetailsFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "airports-service",
        url = "https://aviationweather.gov/api/data/airport",
        fallbackFactory = AirportsDetailsFallbackFactory.class
)
public interface AirportsClient {

    @GetMapping("?format=json&ids={icaoId}")
    List<AirportDetails> getAirportsDetails(@RequestParam String icaoId);
}

