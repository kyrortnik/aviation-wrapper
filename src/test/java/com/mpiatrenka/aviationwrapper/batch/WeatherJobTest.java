package com.mpiatrenka.aviationwrapper.batch;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@SpringBatchTest
class WeatherJobTest {

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Container
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:9.6.12")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");


    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL::getDriverClassName);
    }

    @BeforeAll
    public static void init() {
        POSTGRESQL.start();
    }

    // not obligatory due to container live cycle managwment by https://github.com/testcontainers/moby-ryuk
    @BeforeAll
    public static void tearDown() {
        POSTGRESQL.stop();
    }


    @BeforeEach
    public void cleanUp(@Autowired WeatherJob weatherJob) {
//        optional as job is unique
        this.jobLauncherTestUtils.setJob(weatherJob);
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void test_endToEnd() throws Exception {
        var params = jobLauncherTestUtils.getUniqueJobParameters();

        var jobExecution = jobLauncherTestUtils.launchJob(params);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }
}
