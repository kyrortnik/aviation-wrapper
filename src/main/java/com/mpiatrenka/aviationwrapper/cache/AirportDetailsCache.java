package com.mpiatrenka.aviationwrapper.cache;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface AirportDetailsCache {

    Optional<List<AirportDetails>> getAirportDetails(String icaoId);

    void saveDetails(String icaoId, List<AirportDetails> details);
}
