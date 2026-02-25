package com.mpiatrenka.aviationwrapper.api;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/airports")
@Tag( name = "Airports details", description = "APIs related to operations with Airports details")
public interface AirportsApi {

    @GetMapping("/{icaoId}")
    @Operation(method = "GET", summary = "Get airport details by ICAO id")
    ResponseEntity<List<AirportDetails>> getAirportDetails(@PathVariable String icaoId);
}
