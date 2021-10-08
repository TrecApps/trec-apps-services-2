package com.trecapps.falsehoods.falsehoodSearch.services;


import com.trecapps.base.FalsehoodModel.models.FullFalsehood;
import com.trecapps.base.FalsehoodModel.models.FullPublicFalsehood;
import com.trecapps.base.FalsehoodModel.models.PublicFalsehood;
import com.trecapps.base.FalsehoodModel.models.Severity;
import com.trecapps.base.FalsehoodModel.repos.PublicFalsehoodRecordsRepo;
import com.trecapps.base.FalsehoodModel.repos.PublicFalsehoodRepo;
import com.trecapps.base.InfoResource.models.Institution;
import com.trecapps.base.InfoResource.models.PublicFigure;
import com.trecapps.base.InfoResource.models.Region;
import com.trecapps.falsehoods.falsehoodSearch.config.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class PublicFalsehoodService {

    PublicFalsehoodRepo pfRepo;

	StorageClient s3BucketManager;

	PublicFalsehoodRecordsRepo recordsRepo;

    @Autowired
    public PublicFalsehoodService(@Autowired PublicFalsehoodRepo pfRepo,
									@Autowired StorageClient s3BucketManager,
								  @Autowired PublicFalsehoodRecordsRepo recordsRepo)
    {
        this.pfRepo = pfRepo;
        this.s3BucketManager = s3BucketManager;
		this.recordsRepo = recordsRepo;
    }
    

    
	public List<PublicFalsehood> getSubmittedFalsehoods(int size, int page)
	{
		return pfRepo.getSubmittedFalsehoods(PageRequest.of(page, size));
	}
    
	public List<PublicFalsehood> searchByRegion(Region id, int size, int page)
	{
		return pfRepo.getConfirmedFalsehoodsByRegion(PageRequest.of(page, size), (byte) 20, id);
	}
	
	public List<PublicFalsehood> searchByInstitution(Institution id, int size, int page)
	{
		return pfRepo.getConfirmedFalsehoodsByInstitution(PageRequest.of(page, size), (byte) 20, id);
	}
	
    public List<PublicFalsehood> searchConfirmedFalsehoodsByAttribute(SearchPublicFalsehood search)
    {
    	
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{

    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetween(p, offType, begin, end) : 
    					pfRepo.getConfirmedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official) : 
    					pfRepo.getConfirmedFalsehoodsBetweenAndByOfficial(p, terms, offType, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBefore(p, offType, end) : 
    					pfRepo.getConfirmedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, offType, end, official) : 
    					pfRepo.getConfirmedFalsehoodsBeforeAndByOfficial(p, terms, offType, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    					pfRepo.getConfirmedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    					pfRepo.getConfirmedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoods(p, offType) : 
    					pfRepo.getConfirmedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getConfirmedFalsehoodsByOfficial(p, offType, official) : 
    					pfRepo.getConfirmedFalsehoodsByOfficial(p, terms, offType, official);    				
    			}
    		}
    	}
    }
    
    public List<PublicFalsehood> searchRejectedFalsehoodsByAttribute(SearchPublicFalsehood search)
    {
    	PublicFigure official = search.getOfficial();
    	Date begin = search.getFrom();
    	Date end = search.getTo();
		if(end == null && begin != null)
			end = new Date(Calendar.getInstance().getTime().getTime());
		
    	
    	Severity minSev = search.getMinimum();
    	Severity maxSev = search.getMaximum();
    	
    	byte offType = search.getOfficialType();
    	String terms = search.getTerms();
    	
    	Pageable p = PageRequest.of(search.getPage(), search.getNumberOfEntries() == 0 ? 1 : search.getNumberOfEntries());
    	
    	
    	if(begin != null && end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndBySeverity(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndBySeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, offType, begin, end, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverity(p, terms, offType, begin, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, offType, begin, end, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMinSeverityOfficial(p, terms, offType, begin, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, offType, begin, end, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverity(p, terms, offType, begin, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, offType, begin, end, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByMaxSeverityOfficial(p, terms, offType, begin, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetween(p, offType, begin, end) : 
    					pfRepo.getRejectedFalsehoodsBetween(p, terms, offType, begin, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, offType, begin, end, official) : 
    					pfRepo.getRejectedFalsehoodsBetweenAndByOfficial(p, terms, offType, begin, end, official);
    			}
    		}
    	}
    	else if(end != null)
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, offType, end, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndBySeverity(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, offType, end, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndBySeverityOfficial(p, terms, offType, end, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, offType, end, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverity(p, terms, offType, end, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, offType, end, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMinSeverityOfficial(p, terms, offType, end, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, offType, end, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverity(p, terms, offType, end, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, offType, end, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByMaxSeverityOfficial(p, terms, offType, end, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBefore(p, offType, end) : 
    					pfRepo.getRejectedFalsehoodsBefore(p, terms, offType, end);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, offType, end, official) : 
    					pfRepo.getRejectedFalsehoodsBeforeAndByOfficial(p, terms, offType, end, official);
    			}
    		}
    	}
    	else
    	{
    		if(minSev != null && maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverity(p, offType, maxSev.GetValue(), minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsBySeverity(p, terms, offType, maxSev.GetValue(), minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, offType, maxSev.GetValue(), minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsBySeverityOfficial(p, terms, offType, maxSev.GetValue(), minSev.GetValue(), official);
    			}
    		}
    		else if(minSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverity(p, offType, minSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsByMinSeverity(p, terms, offType, minSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, offType, minSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsByMinSeverityOfficial(p, terms, offType, minSev.GetValue(), official);
    			}
    		}
    		else if(maxSev != null)
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverity(p, offType, maxSev.GetValue()) : 
    					pfRepo.getRejectedFalsehoodsByMaxSeverity(p, terms, offType, maxSev.GetValue());
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, offType, maxSev.GetValue(), official) : 
    					pfRepo.getRejectedFalsehoodsByMaxSeverityOfficial(p, terms, offType, maxSev.GetValue(), official);
    			}
    		}
    		else
    		{
    			if(official == null)
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoods(p, offType) : 
    					pfRepo.getRejectedFalsehoods(p, terms, offType);
    			}
    			else
    			{
    				return (terms == null) ? pfRepo.getRejectedFalsehoodsByOfficial(p, offType, official) : 
    					pfRepo.getRejectedFalsehoodsByOfficial(p, terms, offType, official);
    				
    			}
    		}
    	}
    }
    

    public Mono<FullPublicFalsehood> getFalsehoodById(BigInteger id)
    {		if(!pfRepo.existsById(id))
			return Mono.empty();

		return s3BucketManager.getFalsehoodContents(id + "-PublicFalsehood")
				.map((String contents) -> {
					return new FullPublicFalsehood(contents, pfRepo.getById(id), recordsRepo.findById(id).get());
				});
    }

}
