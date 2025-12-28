package com.example.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {
    @Value("${worker-pool.in-app.thread-count}")
    private int inAppThreadCount;
    @Value("${worker-pool.email.thread-count}")
    private int emailThreadCount;

    @Bean("inAppExecutor")
    public Executor inAppWorkerPool(){
        return new ThreadPoolExecutor (
                inAppThreadCount,inAppThreadCount, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(500),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Bean("emailExecutor")
    public Executor emailWorkerPool(){
        return new ThreadPoolExecutor (
                emailThreadCount,emailThreadCount,0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(500),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
