package com.trecapps.base.FalsehoodModel.models;

import com.trecapps.base.InfoResource.models.Institution;
import com.trecapps.base.InfoResource.models.PublicFigure;
import com.trecapps.base.InfoResource.models.Region;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;


@Entity
@Table
public class PublicFalsehood implements Comparable<PublicFalsehood>{

	@Override
	public int compareTo(PublicFalsehood o) {
		return id.compareTo(o.getId());
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	BigInteger id;
	
	@Column
	byte status;
	// Verification Status
	
	@Transient 
	public static final byte SUBMITTED = 0;	// Has been submitted, but not verified as a false hood
	@Transient 
	public static final byte VERIFIED = 1;	// Has been verified by staff
	@Transient 
	public static final byte CHALLENGED = 2;	// Has been verified, but is now being challenged
	@Transient 
	public static final byte REVERIFIED = 3;	// Has been verified and the challenge has failed
	@Transient 
	public static final byte MODIFIED = 4;	// Has been modified from an earlier version
	@Transient 
	public static final byte OVERTURNED = 5;	// Has previously been verified, but was overturned
	@Transient
	public static final byte REJECTED = 6;
	
	@ManyToOne
	@JoinColumn
	PublicFigure official;
	
	@Column
	byte officialType;
	
	@Transient
	public static final byte POLITICIAN = 0;
	@Transient
	public static final byte LAW_ENFORCEMENT = 1;
	@Transient
	public static final byte INTELLIGENCE = 2;
	@Transient
	public static final byte MILITARY = 3;
	@Transient 
	public static final byte PUBLIC_HEALTH = 4;
	@Transient
	public static final byte ECONOMIST = 5;
	@Transient
	public static final byte ENVIRONMENTALIST = 6;
	@Transient
	public static final byte CORPORATE_EXECUTIVE = 7;
	
	@Transient
	public static final byte OTHER = 15;
	
	@ManyToOne
	@JoinColumn
	Region region;
	
	@ManyToOne
	@JoinColumn
	Institution institution;
	
	@Column
	Severity severity;
	
	@Column
	Date dateMade;
	
	@Column(length = 400)
	String tags;
	

	/**
	 * 
	 */
	public PublicFalsehood() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param status
	 * @param official
	 * @param officialType
	 * @param region
	 * @param institution
	 * @param severity
	 */
	public PublicFalsehood(BigInteger id, byte status, PublicFigure official, byte officialType,
                           Region region, Institution institution, Severity severity, Date dateMade, String tags) {
		super();
		this.id = id;
		this.status = status;
		this.official = official;
		this.officialType = officialType;
		this.region = region;
		this.institution = institution;
		this.severity = severity;
		this.dateMade = dateMade;
		this.tags = tags;
	}
	
	public boolean canUpgrade()
	{
		return severity.GetValue() == (byte)1 || severity.GetValue() == (byte)4;
	}
	
	public boolean upgrade()
	{
		if(!canUpgrade())
			return false;
		severity = Severity.values()[severity.GetValue()-1];
		return true;
	}
	
	public PublicFalsehood clone()
	{
		return new PublicFalsehood(id, status, official, officialType, region, institution, severity, dateMade, tags);
	}
	
	

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the official
	 */
	public PublicFigure getOfficial() {
		return official;
	}

	/**
	 * @param official the official to set
	 */
	public void setOfficial(PublicFigure official) {
		this.official = official;
	}

	/**
	 * @return the officialType
	 */
	public byte getOfficialType() {
		return officialType;
	}

	/**
	 * @param officialType the officialType to set
	 */
	public void setOfficialType(byte officialType) {
		this.officialType = officialType;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * @return the institution
	 */
	public Institution getInstitution() {
		return institution;
	}

	/**
	 * @param institution the institution to set
	 */
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * @return the dateMade
	 */
	public Date getDateMade() {
		return dateMade;
	}

	/**
	 * @param dateMade the dateMade to set
	 */
	public void setDateMade(Date dateMade) {
		this.dateMade = dateMade;
	}

	
}
