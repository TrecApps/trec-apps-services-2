package com.trecapps.base.FalsehoodModel.models;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FullPublicFalsehood {

    String contents;

    PublicFalsehood metadata;

    public FullPublicFalsehood() {
    }
    
    /**
	 * @param contents
	 * @param metadata
	 */
	public FullPublicFalsehood(String contents, PublicFalsehood metadata) {
		super();
		this.contents = contents;
		this.metadata = metadata;
	}

	public FullPublicFalsehood clone()
    {
    	return new FullPublicFalsehood(contents, metadata.clone());
    }

	public FullPublicFalsehood clone(byte severity)
	{
		if(severity < 0 || severity > 7)
			throw new IllegalArgumentException("Severity Parameter out of bounds");

		PublicFalsehood newMeta = metadata.clone();
		newMeta.setStatus(severity);
		return new FullPublicFalsehood(contents, newMeta);
	}

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public PublicFalsehood getMetadata() {
        return metadata;
    }

    public void setMetadata(PublicFalsehood metadata) {
        this.metadata = metadata;
    }

    
}
