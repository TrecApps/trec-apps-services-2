package com.trecapps.base.InfoResource.models;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Container(containerName = "outlets", ru = "400")
@ToString
public class MediaOutletRecords {
    Integer outletId;

    @PartitionKey
    byte partition;

    List<Record> records;
}
