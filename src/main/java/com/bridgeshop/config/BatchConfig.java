package com.bridgeshop.config;

import com.bridgeshop.module.stats.entity.StatsSales;
import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import com.bridgeshop.batch.processor.StatsSalesCategoryItemProcessor;
import com.bridgeshop.batch.processor.StatsSalesItemProcessor;
import com.bridgeshop.batch.reader.StatsSalesCategoryItemReader;
import com.bridgeshop.batch.reader.StatsSalesItemReader;
import com.bridgeshop.batch.writer.StatsSalesCategoryItemWriter;
import com.bridgeshop.batch.writer.StatsSalesItemWriter;
import com.bridgeshop.module.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    private final StatsSalesItemReader statsSalesItemReader;
    private final StatsSalesItemProcessor statsSalesItemProcessor;
    private final StatsSalesItemWriter statsSalesItemWriter;

    private final StatsSalesCategoryItemReader statsSalesCategoryItemReader;
    private final StatsSalesCategoryItemProcessor statsSalesCategoryItemProcessor;
    private final StatsSalesCategoryItemWriter statsSalesCategoryItemWriter;

    @Bean
    public Step StatsSalesStep() {
        return new StepBuilder("StatsSalesStep", jobRepository)
                .<Order, StatsSales>chunk(10)  // chunk 크기 설정
                .reader(statsSalesItemReader)
                .processor(statsSalesItemProcessor)
                .writer(statsSalesItemWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job StatsSalesJob() {
        return new JobBuilder("StatsSalesJob", jobRepository)
//                .repository(jobRepository)
                .start(StatsSalesStep())
                .build();
    }

    @Bean
    public Step StatsSalesCategoryStep() {
        return new StepBuilder("StatsSalesCategoryStep", jobRepository)
                .<StatsSalesCategory, StatsSalesCategory>chunk(10)  // chunk 크기 설정
                .reader(statsSalesCategoryItemReader)
                .processor(statsSalesCategoryItemProcessor)
                .writer(statsSalesCategoryItemWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job StatsSalesCategoryJob() {
        return new JobBuilder("StatsSalesCategoryJob", jobRepository)
                .start(StatsSalesCategoryStep())
                .build();
    }
}
