package com.internetapplication.ws;

import com.internetapplication.ws.security.AppProperties;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class ContactAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactAppWsApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name="AppProperties")
	public AppProperties appProperties() {
		return new AppProperties();
	}


	@Bean
	public JobRepository jobRepository() throws Exception {
		MapJobRepositoryFactoryBean factory
				= new MapJobRepositoryFactoryBean();
		factory.setTransactionManager(transactionManager());
		return (JobRepository) factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}


}
