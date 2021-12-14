package com.trecapps.base.FalsehoodModel.repos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trecapps.base.FalsehoodModel.models.FalsehoodRecords;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehoodRecords;
import com.trecapps.base.InfoResource.config.StorageClient;
import com.trecapps.base.InfoResource.models.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class PublicFalsehoodRecordsRepo// extends CosmosRepository<PublicFalsehoodRecords, BigInteger>
{
    @Autowired
    StorageClient client;

    ObjectMapper mapper = new ObjectMapper();

    void save(PublicFalsehoodRecords records) throws JsonProcessingException {
        if(records.getFalsehoodId() == null)
            throw new NullPointerException("Null Falsehood Id Provided!");

        String name = "Public-Falsehood-Records-" + records.getFalsehoodId();

        client.SubmitJson(name, mapper.writeValueAsString(records.getRecords()), "Trec-Apps-Falsehood", "Falsehood");
    }

    List<Record> retrieveRecords(long id) throws JsonProcessingException {
        String name = "Public-Falsehood-Records-" + id;

        String contents = client.getContents(name, "Falsehood").block();

        return mapper.readValue(contents, new TypeReference<List<Record>>() {
        });
    }
}
