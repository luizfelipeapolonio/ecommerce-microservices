package com.felipe.ecommerce_catalog_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("catalog-task-executor");
    executor.setCorePoolSize(2); // Initialize with 2 threads
    executor.setMaxPoolSize(5); // max up to 5 threads
    executor.setQueueCapacity(10); // If all 5 threads are busy, incoming requests are placed in queue (max 10)
    executor.initialize();
    return executor;
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("catalog-task-scheduler");
    scheduler.setPoolSize(2);
    scheduler.initialize();
    return scheduler;
  }
}
