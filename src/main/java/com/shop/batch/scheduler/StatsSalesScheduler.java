package com.shop.batch.scheduler;

import com.shop.config.BatchConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsSalesScheduler {

    /**
     * jobLauncher
     * - 배치 Job을 실행시키는 역할을 한다.
     * - jobLauncher.run(job, jobParameter); 로직으로 배치를 수행한다. job, jobParameter 를 인자로 받아서 jobExecution을 결과로 반환한다.
     * - 스프링 배치가 실행되면 jobLauncher 빈을 생성하고, jobLauncherApplicationRunner가 자동적으로 jobLauncher을 실행시킨다.
     */
    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(batchConfig.StatsSalesJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException    // 동일한 JobExecution에 대해 Job이 이미 실행 중일 때 발생
                 | JobInstanceAlreadyCompleteException  // Job이 이미 완료된 JobInstance에 대해 다시 실행되려 할 때 발생
                 | JobParametersInvalidException        // 제공된 Job 파라미터가 유효하지 않을 때 발생
                 | org.springframework.batch.core.repository.JobRestartException e) {   // Job을 재시작하려 할 때 발생

            log.error(e.getMessage());
        }
    }
}
