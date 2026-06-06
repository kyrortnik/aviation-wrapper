package com.mpiatrenka.aviationwrapper.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Enables the {@link io.micrometer.observation.annotation.Observed @Observed} annotation so that
 * annotated methods create a span (exported to Tempo via OTLP) and a timer metric (scraped by
 * Prometheus) without any boilerplate at the call site.
 */
@Configuration
public class ObservabilityConfig {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
