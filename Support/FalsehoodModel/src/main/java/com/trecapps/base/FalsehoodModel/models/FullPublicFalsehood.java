package com.trecapps.base.FalsehoodModel.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FullPublicFalsehood {

    String contents;

    PublicFalsehood metadata;

    PublicFalsehoodRecords records;

	List<FalsehoodReference> uses, usedBy;

	public FullPublicFalsehood clone()
    {
    	return new FullPublicFalsehood(contents, metadata.clone(), records, uses, usedBy);
    }

	public FullPublicFalsehood clone(byte severity)
	{
		if(severity < 0 || severity > 7)
			throw new IllegalArgumentException("Severity Parameter out of bounds");

		PublicFalsehood newMeta = metadata.clone();
		newMeta.setStatus(severity);
		return new FullPublicFalsehood(contents, newMeta, records, uses,usedBy);
	}

}
