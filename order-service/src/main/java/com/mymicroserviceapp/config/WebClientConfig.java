package com.mymicroserviceapp.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced //-->client side load balancing
                 // we use this if we have multiple instances for a services
          // and with that we use WebClient.Builder
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
