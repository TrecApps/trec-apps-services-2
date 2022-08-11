package com.trecapps.base.InfoResource.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PublicFigureEntry {

	PublicFigure figure;
	
	String text;

	List<Record> records;

}
