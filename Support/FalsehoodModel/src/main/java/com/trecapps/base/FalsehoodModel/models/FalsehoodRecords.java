package com.trecapps.base.FalsehoodModel.models;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import com.trecapps.base.InfoResource.models.Record;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
@ToString
public class FalsehoodRecords {
    @Id
    BigInteger institutionId;

    @PartitionKey
    byte partition;

    List<Record> records;
}
