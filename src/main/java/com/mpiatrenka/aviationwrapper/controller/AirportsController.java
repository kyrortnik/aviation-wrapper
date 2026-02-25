package com.mpiatrenka.aviationwrapper.controller;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import com.mpiatrenka.aviationwrapper.api.AirportsApi;
import com.mpiatrenka.aviationwrapper.service.AirportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/airports")
@RestController
@RequiredArgsConstructor
public class AirportsController implements AirportsApi {

    private final AirportsService airportsService;

    @Override
    public ResponseEntity<List<AirportDetails>> getAirportDetails(String icaoId) {
        return ResponseEntity.ok(airportsService.getAirportDetails(icaoId));
    }
}
