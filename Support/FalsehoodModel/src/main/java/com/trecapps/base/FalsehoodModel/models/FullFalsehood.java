package com.trecapps.base.FalsehoodModel.models;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FullFalsehood {

	String contents;
	
	Falsehood metadata;
	
	/**
	 * @param contents
	 * @param metadata
	 */
	public FullFalsehood(String contents, Falsehood metadata) {
		super();
		this.contents = contents;
		this.metadata = metadata;
	}

	public FullFalsehood clone()
	{
		return new FullFalsehood(contents, metadata.clone());
	}

	public FullFalsehood clone(byte severity)
	{
		if(severity < 0 || severity > 7)
			throw new IllegalArgumentException("Severity Parameter out of bounds");

		Falsehood newMeta = metadata.clone();
		newMeta.setStatus(severity);
		return new FullFalsehood(contents, newMeta);
	}

	/**
	 * 
	 */
	public FullFalsehood() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the metadata
	 */
	public Falsehood getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Falsehood metadata) {
		this.metadata = metadata;
	}


	
}
