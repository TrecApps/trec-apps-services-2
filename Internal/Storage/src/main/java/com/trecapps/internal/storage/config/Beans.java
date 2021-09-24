package com.trecapps.internal.storage.config;

import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.StorageUri;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class Beans {

    @Value("${azure.storage.blob-endpoint}")
    String endpoint;

    @Value("${azure.storage.account-name}")
    String accountName;

    @Value("${azure.storage.account-key}")
    String accountKey;

    @Bean
    public CloudBlobClient getBlobClient() throws URISyntaxException {
        StorageCredentials cred = new StorageCredentialsAccountAndKey(accountName, accountKey);
        StorageUri uri = new StorageUri(new URI(endpoint));
        return new CloudBlobClient(uri, cred);
    }
}
