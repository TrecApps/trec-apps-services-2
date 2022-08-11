package com.trecapps.base.InfoResource.models;

import lombok.*;

import java.sql.Date;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Record
{
    String recordType;
    String details;
    Date made;
    String userId;
    UUID brandId;
    String specifics;
}
