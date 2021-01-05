package com.fergesch.encaeats.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.*;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig extends AbstractCosmosConfiguration {

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String dbName;

    private AzureKeyCredential azureKeyCredential;

    @Bean
    public CosmosClientBuilder getCosmosClientBuilder() {
        this.azureKeyCredential = new AzureKeyCredential(key);
        DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
        GatewayConnectionConfig gatewayConnectionConfig = new GatewayConnectionConfig();
        return new CosmosClientBuilder()
                .endpoint(uri)
                .credential(azureKeyCredential)
                .directMode(directConnectionConfig, gatewayConnectionConfig);
    }

    @Bean
    public CosmosDatabase getCosmosDatabase() {
        CosmosClientBuilder cosmosClientBuilder = getCosmosClientBuilder();
        CosmosClient cosmosClient = cosmosClientBuilder.buildClient();
        return cosmosClient.getDatabase(dbName);
    }

    @Override
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }
}
