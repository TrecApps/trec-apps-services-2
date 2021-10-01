package com.trecapps.base.InfoResource.models;

import lombok.*;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
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
    Long userId;
    String specifics;
}
