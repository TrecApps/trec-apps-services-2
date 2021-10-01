package com.trecapps.base.InfoResource.models;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class MediaOutletEntry {

	MediaOutlet outlet;
	
	String text;

	List<Record> records;

}
