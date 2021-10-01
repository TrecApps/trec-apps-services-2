package com.trecapps.base.InfoResource.models;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
@ToString
public class RegionRecords {
    Long regionId;

    @PartitionKey
    byte partition;

    List<Record> records;
}
