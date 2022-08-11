package com.trecapps.base.FalsehoodModel.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;


@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FullFalsehood {

	String contents;
	
	Falsehood metadata;

	FalsehoodRecords records;

	List<FalsehoodReference> uses, usedBy;

	public FullFalsehood clone()
	{
		return new FullFalsehood(contents, metadata.clone(), records, uses, usedBy);
	}

	public FullFalsehood clone(byte severity)
	{
		if(severity < 0 || severity > 7)
			throw new IllegalArgumentException("Severity Parameter out of bounds");

		Falsehood newMeta = metadata.clone();
		newMeta.setStatus(severity);
		return new FullFalsehood(contents, newMeta, records, uses, usedBy);
	}

	
}
