package org.sitenv.contentvalidator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.sitenv.contentvalidator.parsers.ParserUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CCDALabResultOrg {

	private static Logger log = LoggerFactory.getLogger(CCDALabResultOrg.class.getName());
	
	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						statusCode;
	private CCDACode						orgCode;
	private CCDAEffTime						effTime;
	private ArrayList<CCDALabResultObs>		resultObs;
	private ArrayList<CCDANotesActivity>	notesActivity;
	private ArrayList<CCDASpecimen>			specimenType;
	
	private CCDAAuthor	author;
	
	public void getAllNotesActivities(HashMap<String, CCDANotesActivity> results) {
		
		if( resultObs != null && resultObs.size() > 0) {
			
			for(CCDALabResultObs res : resultObs) {
				
				res.getAllNotesActivities(results);
			}
		}
	}
	
	public void log() {
		
		log.info(" *** Lab Result Organizer ***");
		
		if(orgCode != null)
			log.info(" Organizer  Code = " + orgCode.getCode());
		
		if(statusCode != null)
			log.info(" Organizer Status  Code = " + statusCode.getCode());
		
		if(effTime != null)
			effTime.log();
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
		for(int k = 0; k < resultObs.size(); k++) {
			resultObs.get(k).log();
		}
		
		if(author != null)
			author.log();
		
		for(int l = 0; l < specimenType.size(); l++) {
			specimenType.get(l).log();
		}
	}
	
	public CCDALabResultOrg()
	{
		templateIds = new ArrayList<CCDAII>();
		resultObs = new ArrayList<CCDALabResultObs>();
		notesActivity = new ArrayList<CCDANotesActivity>();
		specimenType = new ArrayList<>();
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDACode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(CCDACode statusCode) {
		this.statusCode = statusCode;
	}

	public CCDACode getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(CCDACode orgCode) {
		this.orgCode = orgCode;
	}

	public CCDAEffTime getEffTime() {
		return effTime;
	}

	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}

	public ArrayList<CCDALabResultObs> getResultObs() {
		return resultObs;
	}

	public void setResultObs(ArrayList<CCDALabResultObs> robs) {
		
		if(robs != null)
			this.resultObs = robs;
	}

	public CCDAAuthor getAuthor() {
		return author;
	}

	public void setAuthor(CCDAAuthor author) {
		this.author = author;
	}

	public ArrayList<CCDANotesActivity> getNotesActivity() {
		return notesActivity;
	}

	public void setNotesActivity(ArrayList<CCDANotesActivity> notesActivity) {
		this.notesActivity = notesActivity;
	}

	public ArrayList<CCDASpecimen> getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(ArrayList<CCDASpecimen> specimenType) {
		this.specimenType = specimenType;
	}

	public void getAllSpecimens(HashMap<String, CCDASpecimen> specs) {
		
		if(specimenType != null) {
			
			for(CCDASpecimen s: specimenType) {
				
				if(s.getSpecimenType() != null 
						&& !StringUtils.isEmpty(s.getSpecimenType().getCode())
						&& !specs.containsKey(s.getSpecimenType().getCode())) {
					specs.put(s.getSpecimenType().getCode(), s);
				}
				
			}
			
			for(CCDALabResultObs obs: resultObs) {
				obs.getAllSpecimens(specs);
			}
			
		}
		
	}

	
}
