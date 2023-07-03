package com.microservices.demo.common.config;

import com.microservices.demo.config.RetryConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
    private final RetryConfigData retryConfigData;

    public RetryConfig(RetryConfigData retryConfigData) {
        this.retryConfigData = retryConfigData;
    }

    @Bean
    public RetryTemplate retryTemplate() { //cung cấp cơ chế tái thử lại cho các hoạt động thất bại.

        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();//là một chính sách back-off (khoảng thời gian chờ giữa các lần thử lại) theo mô hình tăng theo cấp số nhân.
        exponentialBackOffPolicy.setInitialInterval(retryConfigData.getInitialIntervalMs());//khoảng thời gian ban đầu
        exponentialBackOffPolicy.setMaxInterval(retryConfigData.getMaxIntervalMs());//khoảng thời gian tối đa
        exponentialBackOffPolicy.setMultiplier(retryConfigData.getMultiplier());//hệ số nhân
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();// là một chính sách đơn giản cho số lần thử lại tối đa
        simpleRetryPolicy.setMaxAttempts(retryConfigData.getMaxAttempts());

        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        return retryTemplate;
    }
}
