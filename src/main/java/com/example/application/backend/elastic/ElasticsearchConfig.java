package com.example.application.backend.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = "project.env.local",
        havingValue = "false",
        matchIfMissing = false)
public class ElasticsearchConfig {


    @Value("${elastic.endpoint.host}")
    String endpointHost;

    /**
     * Create the low-level client
     */
    @Bean
    public RestClient restClient() {
        final CredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(
                        "bigevents",
                        "platfozaBigeventsElastic123$"
                ));

        return RestClient.builder(
                        new HttpHost(endpointHost,443 , "https"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                                .setDefaultCredentialsProvider(credentialProvider)
                                .addInterceptorLast((HttpResponseInterceptor)
                                        (response, context) ->
                                                response.addHeader("X-Elastic-Product", "Elasticsearch"))
                        //.setSSLContext(sslContext)
                )
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Content-type", "application/json"),
                })

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
