package com.mpiatrenka.aviationwrapper.batch.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class FileTransferringTasklet implements Tasklet {

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        var jobParams = contribution.getStepExecution().getJobParameters();
        var filePath = jobParams.getString("input.file");

        Path source = Paths.get(filePath);
        Path target = Paths.get("staging", source.toFile().getName());

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        return RepeatStatus.FINISHED;
    }
}
