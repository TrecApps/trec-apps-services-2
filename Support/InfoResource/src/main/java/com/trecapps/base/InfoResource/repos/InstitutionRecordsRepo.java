package com.trecapps.base.InfoResource.repos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trecapps.base.InfoResource.config.StorageClient;
import com.trecapps.base.InfoResource.models.Record;
import com.trecapps.base.InfoResource.models.InstitutionRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstitutionRecordsRepo {
    @Autowired
    StorageClient client;

    ObjectMapper mapper = new ObjectMapper();

    void save(InstitutionRecords records) throws JsonProcessingException {
        if(records.getInstitutionId() == null)
            throw new NullPointerException("Null Institution Id Provided!");

        String name = "Institution-Records-" + records.getInstitutionId();

        client.SubmitJson(name, mapper.writeValueAsString(records.getRecords()), "Trec-Apps-Resource", "Resource");
    }

    List<Record> retrieveRecords(long id) throws JsonProcessingException {
        String name = "Institution-Records-" + id;

        String contents = client.getContents(name, "Resource").block();

        return mapper.readValue(contents, new TypeReference<List<Record>>() {
        });
    }
}
