package com.mpiatrenka.aviationwrapper.batch;

import com.mpiatrenka.aviationwrapper.batch.tasklet.FileTransferringTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JobConfig {

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("weatherJob", jobRepository)
                .start(firstStep(jobRepository, transactionManager))
                .build();
    }

    //    taskletStep
    @Bean
    public TaskletStep firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(new FileTransferringTasklet(), transactionManager)
                .build();
    }

}
