package com.trecapps.internal.storage.repos;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.microsoft.azure.storage.blob.CloudBlobClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Repository
public class StorageRepo {

    CloudBlobClient client;

    @Autowired
    StorageRepo(CloudBlobClient client1)
    {
        client = client1;
    }

    public String getContent(String containerName, String blobName, boolean useBase64) throws URISyntaxException, StorageException, IOException {
        CloudBlobContainer container = client.getContainerReference(containerName);


        CloudBlob blob = container.getBlobReferenceFromServer(blobName);

        BlobInputStream input = blob.openInputStream();

        byte[] data = input.readAllBytes();
        input.close();


        return useBase64 ? Base64.getEncoder().encodeToString(data) : new String(data, StandardCharsets.UTF_8);
    }

    public void uploadContent(String containerName, String blobName, String content, boolean usesBase64) throws URISyntaxException, StorageException, IOException {
        CloudBlobContainer container = client.getContainerReference(containerName);


        CloudBlob blob = container.getBlobReferenceFromServer(blobName);
        blob.acquireLease();
        byte[] bytes = usesBase64 ? Base64.getDecoder().decode(content) : content.getBytes(StandardCharsets.UTF_8);

        blob.uploadFromByteArray(bytes, 0, bytes.length);

        blob.breakLease(1);
    }

    public void deleteContent(String containerName, String blobName) throws URISyntaxException, StorageException {
        CloudBlobContainer container = client.getContainerReference(containerName);

        CloudBlob blob = container.getBlobReferenceFromServer(blobName);
        blob.delete();

    }
}
