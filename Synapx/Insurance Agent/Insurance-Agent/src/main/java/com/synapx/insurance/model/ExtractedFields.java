package com.synapx.insurance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtractedFields {
    
    // Policy Information
    private String policyNumber;
    private String policyholdername;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;
    
    // Incident Information
    private LocalDate incidentDate;
    private LocalTime incidentTime;
    private String location;
    private String description;
    
    // Involved Parties
    private String claimant;
    private List<String> thirdParties;
    private ContactDetails contactDetails;
    
    // Asset Details
    private String assetType;
    private String assetId;
    private BigDecimal estimatedDamage;
    
    // Mandatory Fields
    private String claimType; // "auto", "property", "injury", etc.
    private List<String> attachments;
    private BigDecimal initialEstimate;
    
    // Metadata
    private LocalDateTime extractionTime;
    private Double confidenceScore; // 0-1, confidence of extraction
}