package com.synapx.insurance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.synapx.insurance.model.ExtractedFields;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FieldValidationService {
    
    private static final List<String> MANDATORY_FIELDS = Arrays.asList(
        "policyNumber",
        "policyholdername",
        "incidentDate",
        "location",
        "description",
        "claimant",
        "claimType",
        "estimatedDamage"
    );
    
    public List<String> validateFields(ExtractedFields fields) {
        List<String> missingFields = new ArrayList<>();
        
        if (StringUtils.isBlank(fields.getPolicyNumber())) {
            missingFields.add("policyNumber");
        }
        
        if (StringUtils.isBlank(fields.getPolicyholdername())) {
            missingFields.add("policyholdername");
        }
        
        if (fields.getIncidentDate() == null) {
            missingFields.add("incidentDate");
        }
        
        if (StringUtils.isBlank(fields.getLocation())) {
            missingFields.add("location");
        }
        
        if (StringUtils.isBlank(fields.getDescription())) {
            missingFields.add("description");
        }
        
        if (StringUtils.isBlank(fields.getClaimant())) {
            missingFields.add("claimant");
        }
        
        if (StringUtils.isBlank(fields.getClaimType())) {
            missingFields.add("claimType");
        }
        
        if (fields.getEstimatedDamage() == null || fields.getEstimatedDamage().compareTo(BigDecimal.ZERO) <= 0) {
            missingFields.add("estimatedDamage");
        }
        
        if (fields.getContactDetails() == null || 
            (StringUtils.isBlank(fields.getContactDetails().getEmail()) && 
             StringUtils.isBlank(fields.getContactDetails().getPhoneNumber()))) {
            missingFields.add("contactDetails");
        }
        
        return missingFields;
    }
    
    
    public boolean hasConsistencyIssues(ExtractedFields fields) {
        // Check for inconsistencies like future incident date
        if (fields.getIncidentDate() != null && fields.getIncidentDate().isAfter(LocalDate.now())) {
            return true;
        }
        
        // Check for unrealistic damage amounts
        if (fields.getEstimatedDamage() != null && 
            fields.getEstimatedDamage().compareTo(new BigDecimal("10000000")) > 0) {
            return true;
        }
        
        return false;
    }
}