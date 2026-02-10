package com.synapx.insurance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synapx.insurance.model.ClaimsResponse;
import com.synapx.insurance.model.ExtractedFields;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoutingService {
    
    @Autowired
    private FieldValidationService validationService;
    
    public ClaimsResponse routeClaim(ExtractedFields fields, List<String> missingFields) {
        ClaimsResponse response = new ClaimsResponse();
        response.setExtractedFields(fields);
        response.setMissingFields(missingFields);
        response.setTimestamp(LocalDateTime.now());
        response.setFlags(new ArrayList<>());
        
        // Step 1: Check for mandatory fields
        if (!missingFields.isEmpty()) {
            response.setRecommendedRoute("MANUAL_REVIEW");
            response.setReasoning("Missing mandatory fields: " + String.join(", ", missingFields));
            response.getFlags().add("INCOMPLETE_DATA");
            return response;
        }
        
        // Step 2: Check for investigation flags
        String description = fields.getDescription();
        if (containsFraudIndicators(description)) {
            response.setRecommendedRoute("INVESTIGATION_QUEUE");
            response.setReasoning("Potential fraud indicators detected in description");
            response.getFlags().add("FRAUD_RISK");
            return response;
        }
        
        // Step 3: Check for consistency issues
        if (validationService.hasConsistencyIssues(fields)) {
            response.setRecommendedRoute("MANUAL_REVIEW");
            response.setReasoning("Inconsistencies detected in claim data");
            response.getFlags().add("CONSISTENCY_CHECK");
            return response;
        }
        
        // Step 4: Check claim type
        if ("INJURY".equalsIgnoreCase(fields.getClaimType())) {
            response.setRecommendedRoute("SPECIALIST_QUEUE");
            response.setReasoning("Injury claims require specialist review");
            response.getFlags().add("INJURY_CLAIM");
            return response;
        }
        
        // Step 5: Check damage amount
        if (fields.getEstimatedDamage() != null && 
            fields.getEstimatedDamage().compareTo(new BigDecimal("25000")) < 0) {
            response.setRecommendedRoute("FAST_TRACK");
            response.setReasoning("Low-value claim eligible for fast-track processing");
            response.getFlags().add("LOW_VALUE");
            return response;
        }
        
        // Default: Standard processing
        response.setRecommendedRoute("STANDARD_QUEUE");
        response.setReasoning("Claim routed to standard processing queue");
        response.getFlags().add("STANDARD");
        return response;
    }
    
    private boolean containsFraudIndicators(String description) {
        if (description == null) return false;
        
        String[] fraudKeywords = {"fraud", "inconsistent", "staged", "suspicious", "claim jumping"};
        String lowerDesc = description.toLowerCase();
        
        for (String keyword : fraudKeywords) {
            if (lowerDesc.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
}