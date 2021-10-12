package com.trecapps.base.InfoResource.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Container(containerName = "institutions", ru = "400")
@ToString
public class InstitutionRecords
{
    Long institutionId;

    @PartitionKey
    byte partition;

    List<Record> records;
}
