package com.trecapps.base.InfoResource.config;

//import com.azure.core.credential.AzureKeyCredential;
//import com.azure.cosmos.CosmosClientBuilder;
//import com.azure.cosmos.DirectConnectionConfig;
//import com.azure.cosmos.GatewayConnectionConfig;
//import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
//import com.azure.spring.data.cosmos.config.CosmosConfig;
//import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableCosmosRepositories
public class CosmosConfigurer// extends AbstractCosmosConfiguration
{
    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.secondaryKey}")
    private String secondaryKey;

    @Value("${azure.cosmos.database}")
    private String dbName;

    @Value("${azure.cosmos.queryMetricsEnabled}")
    private boolean queryMetricsEnabled;

//    private AzureKeyCredential azureKeyCredential;
//
//    @Bean
//    public CosmosClientBuilder getCosmosClientBuilder() {
//        this.azureKeyCredential = new AzureKeyCredential(key);
//        DirectConnectionConfig directConnectionConfig = new DirectConnectionConfig();
//        GatewayConnectionConfig gatewayConnectionConfig = new GatewayConnectionConfig();
//        return new CosmosClientBuilder()
//                .endpoint(uri)
//                .credential(azureKeyCredential)
//                .directMode(directConnectionConfig, gatewayConnectionConfig);
//    }
//
//    @Override
//    public CosmosConfig cosmosConfig() {
//        return CosmosConfig.builder()
//                .enableQueryMetrics(queryMetricsEnabled)
//                .build();
//    }
//
//    public void switchToSecondaryKey() {
//        this.azureKeyCredential.update(secondaryKey);
//    }

    //@Override
    protected String getDatabaseName() {
        return dbName;
    }
}
