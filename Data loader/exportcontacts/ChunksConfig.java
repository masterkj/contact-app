package com.internetapplication.ws.jobs.exportcontacts;

import com.internetapplication.ws.jobs.exportcontacts.chunks.LineProcessor;
import com.internetapplication.ws.jobs.exportcontacts.chunks.LinesReader;
import com.internetapplication.ws.jobs.exportcontacts.chunks.LinesWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ChunksConfig {

    @Autowired private JobBuilderFactory jobs;

    @Autowired private StepBuilderFactory steps;

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
        factory.setTransactionManager(transactionManager());
        return (JobRepository) factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean
    public ItemReader<Line> itemReader() {
        return new LinesReader();
    }

    @Bean
    public ItemProcessor<Line, Line> itemProcessor() {
        return new LineProcessor();
    }

    @Bean
    public ItemWriter<Line> itemWriter() {
        return new LinesWriter();
    }

    @Bean
    protected Step processLines(ItemReader<Line> reader, ItemProcessor<Line, Line> processor, ItemWriter<Line> writer) {
        return steps.get("processLines").<Line, Line> chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job job() {
        return jobs
                .get("chunksJob")
                .start(processLines(itemReader(), itemProcessor(), itemWriter()))
                .build();
    }

}
