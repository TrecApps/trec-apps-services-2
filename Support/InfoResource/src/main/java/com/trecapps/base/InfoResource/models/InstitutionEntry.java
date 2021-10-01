package com.trecapps.base.InfoResource.models;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InstitutionEntry {

	Institution institution;
	
	String contents;

	List<Record> records;

}
