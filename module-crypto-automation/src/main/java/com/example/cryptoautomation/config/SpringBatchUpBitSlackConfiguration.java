package com.example.cryptoautomation.config;

import com.example.cryptoautomation.service.UpBitSlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SpringBatchUpBitSlackConfiguration {
    private final UpBitSlackService upBitSlackService;

    @Bean
    public Job upBitSlackJob(JobRepository jobRepository, Step upBitSlackStep) {
        return new JobBuilder("upBitSlackJob", jobRepository)
                .start(upBitSlackStep)
                .build();
    }
    @Bean
    public Step upBitSlackStep(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("upBitSlackStep", jobRepository)
                .tasklet(tasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet tasklet() {
        return (((contribution, chunkContext) -> {
//            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
//            String market = String.valueOf(jobParameters.get("market").toString());
            String market = String.valueOf("KRW-BTC");

            upBitSlackService.execute(market);

            return RepeatStatus.FINISHED;
        }));

    }
}
