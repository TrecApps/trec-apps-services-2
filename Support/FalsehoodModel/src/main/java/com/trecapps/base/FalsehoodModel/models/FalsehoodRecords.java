package com.trecapps.base.FalsehoodModel.models;

import com.trecapps.base.InfoResource.models.Record;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Container(containerName = "mFalsehoods", ru = "400")
@ToString
public class FalsehoodRecords {
    @Id
    BigInteger falsehoodId;

    //@PartitionKey
    byte partition;

    List<Record> records;
}
