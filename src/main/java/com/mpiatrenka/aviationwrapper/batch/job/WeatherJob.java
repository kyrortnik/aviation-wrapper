package com.mpiatrenka.aviationwrapper.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherJob implements Job {

    private final JobRepository jobRepository;

    @Override
    public String getName() {
        return "weather-job";
    }

    @Override
    public void execute(JobExecution execution) {
        log.info("Weather job started");
        log.info("Weather job finished");
        execution.setStatus(BatchStatus.COMPLETED);
        jobRepository.update(execution);
    }
}
