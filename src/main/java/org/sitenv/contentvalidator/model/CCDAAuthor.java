package org.sitenv.contentvalidator.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.sitenv.contentvalidator.dto.ContentValidationResult;
import org.sitenv.contentvalidator.dto.enums.ContentValidationResultLevel;
import org.sitenv.contentvalidator.parsers.ParserUtilities;

public class CCDAAuthor {
	
	private static Logger log = Logger.getLogger(CCDAAuthor.class.getName());
	
	private ArrayList<CCDAII>    			templateId;
	private CCDAEffTime          			effTime;
	private ArrayList<CCDAII>    			authorIds;
	private CCDADataElement					authorFirstName;
	private CCDADataElement					authorLastName;
	private CCDADataElement					authorName;
	private ArrayList<CCDAII>				repOrgIds;
	private ArrayList<CCDATelecom>			telecoms;
	private CCDADataElement					orgName;
	
	public CCDAAuthor() {
		
		templateId = new ArrayList<CCDAII>();
		authorIds = new ArrayList<CCDAII>();
		repOrgIds = new ArrayList<CCDAII>();
		telecoms = new ArrayList<CCDATelecom>();
		
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}
	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
	}
	public CCDAEffTime getEffTime() {
		return effTime;
	}
	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}
	public ArrayList<CCDAII> getAuthorIds() {
		return authorIds;
	}
	public void setAuthorIds(ArrayList<CCDAII> authorIds) {
		this.authorIds = authorIds;
	}
	public ArrayList<CCDAII> getRepOrgIds() {
		return repOrgIds;
	}
	public void setRepOrgIds(ArrayList<CCDAII> repOrgIds) {
		this.repOrgIds = repOrgIds;
	}
	public ArrayList<CCDATelecom> getTelecoms() {
		return telecoms;
	}
	public void setTelecoms(ArrayList<CCDATelecom> telecoms) {
		this.telecoms = telecoms;
	}
	public CCDADataElement getOrgName() {
		return orgName;
	}
	public void setOrgName(CCDADataElement orgName) {
		this.orgName = orgName;
	}

	public CCDADataElement getAuthorFirstName() {
		return authorFirstName;
	}

	public void setAuthorFirstName(CCDADataElement authorFirstName) {
		this.authorFirstName = authorFirstName;
	}

	public CCDADataElement getAuthorLastName() {
		return authorLastName;
	}

	public void setAuthorLastName(CCDADataElement authorLastName) {
		this.authorLastName = authorLastName;
	}

	public CCDADataElement getAuthorName() {
		return authorName;
	}

	public void setAuthorName(CCDADataElement authorName) {
		this.authorName = authorName;
	}
	
	public void log() {
		
		log.info("***Author Entry ***");
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		
		for(int k = 0; k < authorIds.size(); k++) {
			log.info(" Author Id [" + k + "] = " + templateId.get(k).getRootValue());
			log.info(" Author Id Ext [" + k + "] = " + templateId.get(k).getExtValue());
		}
		
		for(int l = 0; l < repOrgIds.size(); l++) {
			log.info(" Rep Org Id [" + l + "] = " + templateId.get(l).getRootValue());
			log.info(" Rep Org Id Ext [" + l + "] = " + templateId.get(l).getExtValue());
		}
		
		
		if(effTime != null) 
			effTime.log();
		
		if(authorFirstName != null) {
			log.info(" Author First Name = " + authorFirstName.getValue());
		}
		
		if(authorLastName != null) {
			log.info(" Author Last Name = " + authorLastName.getValue());
		}
		
		if(authorName != null) {
			log.info(" Author  Name = " + authorName.getValue());
		}
		
		if(orgName != null) {
			log.info(" Rep Org  Name = " + orgName.getValue());
		}
		
	}	
	
	public void matches(CCDAAuthor subAuthor, ArrayList<ContentValidationResult> results, String elName) {
		
		log.info(" Comparing data for Author Template Ids: ");

		// Compare template Ids 
		String elementName = "Comapring Author Template Ids for : " + elName;		 // Not mandatory so skipping
		// ParserUtilities.compareTemplateIds(templateId, subAuthor.getTemplateId(), results, elementName);
		
		// Compare Author Ids 
		elementName = "Comapring Author Ids for : " + elName; // Not mandatory so skipping
		// ParserUtilities.compareTemplateIds(authorIds, subAuthor.getAuthorIds(), results, elementName);
		
		// Compare Rep Org Ids 		
		elementName = "Comapring Rep Org Ids for : " + elName; // Not mandatory so skipping
		// ParserUtilities.compareTemplateIds(repOrgIds, subAuthor.getRepOrgIds(), results, elementName);

		// Compare Effective Times
		elementName = "Comparing Author Time for " + elName; 
		ParserUtilities.compareEffectiveTimeValue(effTime, subAuthor.getEffTime(), results,
				elementName);
		// Validate Times
		ParserUtilities.validateTimeValueLengthDateTimeAndTimezoneDependingOnPrecision(subAuthor.getEffTime(), results,
				elementName, 
				(elName != null && !elName.isEmpty()) ? elName.replaceFirst(" , Comparing ", "") : elName,
				-1, true);
		
		// Compare Org Name 
		elementName = "Comparing Author Organization Name for " + elName;
		ParserUtilities.compareDataElementText(orgName, subAuthor.getOrgName(), results, elementName);
	}
	
    public static void compareAuthors(ArrayList<CCDAAuthor> refAuths, ArrayList<CCDAAuthor> subAuths, 
    		ArrayList<ContentValidationResult> results, String elName) {
		log.info(" Comparing data for Author.");
		log.info(" Ref Model Auth Size = " + (refAuths != null ? refAuths.size() : 0));
		log.info(" Sub Model Auth Size = " + (subAuths != null ? subAuths.size() : 0));    	
    	
    	if (refAuths != null && refAuths.size() != 0) { // If no authors in scenario (ref) file, skip the comparison 			
			for (CCDAAuthor auth : refAuths) {
				
				log.info("Checking Ref Author with Sub Authors ");
				if(auth.getEffTime() != null && 
						auth.getEffTime().getValuePresent()
						&& !isProvenancePresent(auth.getEffTime(), auth.getOrgName(), subAuths)) {					
					
					// Note: This is the only result that is actually reported.
					// Many errors are generated in isProvenancePresent sub-routines but there's no way to connect them
					// to a specific sub which actually had the issue (since match can be in any location) so instead the results
					// generated externally are used as a reference for a boolean result which triggers this error 
					// vs adding the unique errors themselves
//					String message = "The scenario requires " + elName
//							+ " (Time: Value) Provenance data which was not found in the submitted data. The scenario value is "
//							+ auth.getEffTime().getValue().getValue()
//							+ " and a submitted value must at a minimum match the 8-digit date portion of the data.";
					final boolean isOrgNameNonNullAndPopulated = 
							auth.getOrgName() != null && auth.getOrgName().getValue() != null && !auth.getOrgName().getValue().isEmpty();
					String message = "The scenario requires " + elName
							+ " Provenance data of time" + (isOrgNameNonNullAndPopulated ? " and/or representedOrganization/name" : "") 
							+ " which was not found in the submitted data. "
							+ "The scenario time value is " + auth.getEffTime().getValue().getValue()
							+ " and a submitted time value should at a minimum match the 8-digit date portion of the data."
							+ (isOrgNameNonNullAndPopulated ? " The scenario representedOrganization/name value is " + auth.getOrgName().getValue()
							+ " and a submitted name should match. One or all of the prior issues exist and must be resolved." : "");																						
							
					ContentValidationResult rs = new ContentValidationResult(message,
							ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0");
					results.add(rs);
				}
				else {
					log.info(" Found Provenance data, nothing else to do ..");
				}
			}
			
			// Validate time value in sub author time value instances specifically (not a comparison)
			// Results are added as individual errors
			if (subAuths != null && subAuths.size() > 0) {
				log.info("starting subAuth validation routine");
				final String localElName = "Author Provenance";
				final boolean isSub = true;
				int subAuthIndex = -1;
				for (CCDAAuthor subAuth : subAuths) {
					subAuthIndex++;
					if (subAuth.getEffTime() != null) {
						log.info("validating subauth at index " + subAuthIndex);
						ParserUtilities.validateTimeValueLengthDateTimeAndTimezoneDependingOnPrecision(
								subAuth.getEffTime(), results, localElName, elName, subAuthIndex, isSub);
					} else {
						log.info("subAuth at index " + subAuthIndex + " is null" );
					}
				}
			} else {
				log.info("subAuths is null or empty");
			}
			
			// Compare Author Sizes			
			// It's invalid to fire an error if ref is less than or equal to sub auth size
			if (refAuths != null && subAuths != null && 
					!(refAuths.size() <= subAuths.size())) {
				ContentValidationResult rs = new ContentValidationResult(
						"The scenario requires a total of " + refAuths.size() + " Author Entries for " + elName
								+ ", however the submitted data had only " + subAuths.size() + " entries.",
						ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0");
						results.add(rs);
			}
			
    	} else {
    		log.info("Skipping compareAuthors due to empty refAuths.");
    	}		
	}
    
    public static boolean isProvenancePresent(CCDAEffTime effTime, CCDADataElement name, ArrayList<CCDAAuthor> subAuths) {
    	
    	boolean retVal = false;
    	String elName = "Comparing Author Provenance Data";
    	ArrayList<ContentValidationResult> res = new ArrayList<ContentValidationResult>();
    	
    	if (subAuths == null) {
    		log.info("subAuths is null, skipping: " + elName);
    	} else {
	    	for(CCDAAuthor auth : subAuths) {
	    		ParserUtilities.compareEffectiveTimeValue(effTime, auth.getEffTime(), res, elName);
	    		
	    		ParserUtilities.compareDataElementText(name, auth.getOrgName(), res, elName);
	    		
	    		if(res != null && res.size() == 0 ) {
	    			
	    			log.info(" Matched Provenance Data ");
	    			retVal = true;
	    			break;
	    		}
	    		else {
	    			res.clear();
	    		}
	    	}
		}
    	
    	return retVal;
    }    
    
    // This does not seem to be used....
    public static boolean isProvenancePresent(CCDAEffTime effTime, CCDADataElement name, CCDAAuthor subAuth) {
    	
    	boolean retVal = false;
    	String elName = "Comparing Author Provenance Data";
    	ArrayList<ContentValidationResult> res = new ArrayList<ContentValidationResult>();   	
    	
    	ParserUtilities.compareEffectiveTimeValueWithExactMatchFullPrecision(effTime, subAuth.getEffTime(), res, elName);
    		
    	ParserUtilities.compareDataElementText(name, subAuth.getOrgName(), res, elName);
    		
    	if(res != null && res.size() == 0 ) {
    			
    		log.info(" Matched Provenance Data ");
    		retVal = true;
    			
    	}
    	else {
    		log.info(" Provenance data did not match ");
    		retVal = false;
    	}
    	
    	return retVal;
    }
    
	/**
	 * Compare if the root section-level author info in this reference author is present in submitted author
	 */
	public static void compareSectionLevelAuthor(String elName, CCDAAuthor refAuthor, CCDAAuthor subAuthor,
			ArrayList<ContentValidationResult> results) {		
		log.info("Comparing " + elName + ", section-level-only author");
		log.info("Ref Section Author " + (refAuthor != null ? "found" : "not found"));
		log.info("Sub Section Author " + (subAuthor != null ? "found" : "not found"));
		
		if (refAuthor != null && subAuthor != null) {
			refAuthor.matches(subAuthor, results, elName);
		} else if (refAuthor != null && subAuthor == null) {
			ContentValidationResult rs = new ContentValidationResult(
					"The scenario requires Provenance data for " + elName
							+ " however the submitted file does not contain the Provenance data for " + elName + ".",
					ContentValidationResultLevel.ERROR, "/ClinicalDocument", "0");
			results.add(rs);
		} else {
			log.info("Author is null in the reference data, nothing to do");
		}		
	}    

}
