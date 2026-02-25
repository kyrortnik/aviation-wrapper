package com.mpiatrenka.aviationwrapper.fallback;

import com.mpiatrenka.aviationwrapper.client.AirportsClient;
import com.mpiatrenka.aviationwrapper.exception.AirportDetailsExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AirportsDetailsFallbackFactory implements FallbackFactory<AirportsClient> {

    @Override
    public AirportsClient create(Throwable cause) {
        return id -> {
            log.error("Airport details service failed", cause);
            throw new AirportDetailsExternalServiceException("Airport details service failed", cause);
        };
    }
}
