package com.bangguddle.ownbang.global.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.bangguddle.ownbang.domain.search.repository")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${ELASTIC_SEARCH_HOST}")
    private String host;

    @Value("${ELASTIC_SEARCH_PORT}")
    private String port;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" +  port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}