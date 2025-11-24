package com.felipe.ecommerce_discount_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(5);
    threadPoolTaskScheduler.setThreadNamePrefix("promotion-scheduler-");
    threadPoolTaskScheduler.initialize();
    return threadPoolTaskScheduler;
  }
}
