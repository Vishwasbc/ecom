package com.example.orders.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ProductServiceClientConfig {
    @LoadBalanced
    @Bean
    public RestClient.Builder restClientBuilderLb() {
        return RestClient.builder();
    }

    //Modification needed to avoid circular dependency
    @Primary
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
    @Bean
    public ProductServiceClient productServiceInterface(@LoadBalanced RestClient.Builder restClientBuilder){
        RestClient restClient = restClientBuilder
                .baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,(((request, response) -> Optional.empty())))
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();
        return factory.createClient(ProductServiceClient.class);
    }
}
