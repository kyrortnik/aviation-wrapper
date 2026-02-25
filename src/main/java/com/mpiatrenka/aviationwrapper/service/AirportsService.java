package com.mpiatrenka.aviationwrapper.service;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;

import java.util.List;

public interface AirportsService {

    List<AirportDetails> getAirportDetails(String icaoId);
}
