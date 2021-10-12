package com.trecapps.base.InfoResource.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Container(containerName = "regions", ru = "400")
@ToString
public class RegionRecords {
    Long regionId;

    @PartitionKey
    byte partition;

    List<Record> records;
}
