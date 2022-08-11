package com.trecapps.base.InfoResource.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MediaOutletEntry {

	MediaOutlet outlet;
	
	String text;

	List<Record> records;

}
