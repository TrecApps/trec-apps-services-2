package com.trecapps.base.InfoResource.models;

import org.springframework.stereotype.Component;

@Component
public class InstitutionEntry {

	Institution institution;
	
	String contents;

	/**
	 * 
	 */
	public InstitutionEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param institution
	 * @param contents
	 */
	public InstitutionEntry(Institution institution, String contents) {
		super();
		this.institution = institution;
		this.contents = contents;
	}

	/**
	 * @return the insitution
	 */
	public Institution getInstitution() {
		return institution;
	}

	/**
	 * @param insitution the insitution to set
	 */
	public void setInstitution(Institution insitution) {
		this.institution = insitution;
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
	
	
}
