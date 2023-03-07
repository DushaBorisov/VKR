package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = "project.env.local",
        havingValue = "true",
        matchIfMissing = true)
public class ElasticsearchConfigLocal {


    @Value("${elastic.endpoint.host}")
    String endpointHost;

    /**
     * Create the low-level client
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                        new HttpHost(endpointHost, 9200, "http"))
                .build();

    }

    /**
     * Create the transport with a Jackson mapper
     */
    @Bean
    public ElasticsearchTransport elsTransport() {
        return new RestClientTransport(
                restClient(), new JacksonJsonpMapper());
    }

    /**
     * Create the API client
     */
    @Bean
    public ElasticsearchClient elsClient() {
        return new ElasticsearchClient(elsTransport());

    }

}
