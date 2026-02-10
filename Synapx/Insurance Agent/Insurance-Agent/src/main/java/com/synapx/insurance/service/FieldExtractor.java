package com.synapx.insurance.service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.synapx.insurance.model.ContactDetails;
import com.synapx.insurance.model.ExtractedFields;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FieldExtractor {
    
    // Regex patterns for field extraction
    private static final Pattern POLICY_PATTERN = 
        Pattern.compile("(?:Policy\\s*(?:Number|No\\.?|#)?[:\\s]*)?([A-Z0-9\\-]{8,20})", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern DATE_PATTERN = 
        Pattern.compile("\\b(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})\\b");
    
    private static final Pattern AMOUNT_PATTERN = 
        Pattern.compile("\\$?\\s*([0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{2})?)");
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("\\b(?:\\+?1[\\s.-]?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}\\b");
    
    public ExtractedFields extractFields(String documentText) {
        ExtractedFields fields = new ExtractedFields();
        
        fields.setPolicyNumber(extractPolicyNumber(documentText));
        fields.setPolicyholdername(extractPolicyholder(documentText));
        fields.setIncidentDate(extractIncidentDate(documentText));
        fields.setIncidentTime(extractIncidentTime(documentText));
        fields.setLocation(extractLocation(documentText));
        fields.setDescription(extractDescription(documentText));
        fields.setClaimant(extractClaimant(documentText));
        fields.setThirdParties(extractThirdParties(documentText));
        fields.setContactDetails(extractContactDetails(documentText));
        fields.setAssetType(extractAssetType(documentText));
        fields.setAssetId(extractAssetId(documentText));
        fields.setEstimatedDamage(extractEstimatedDamage(documentText));
        fields.setClaimType(extractClaimType(documentText));
        fields.setInitialEstimate(extractInitialEstimate(documentText));
        fields.setExtractionTime(LocalDateTime.now());
        
        return fields;
    }
    
    private String extractPolicyNumber(String text) {
        Matcher matcher = POLICY_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private LocalDate extractIncidentDate(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                return LocalDate.parse(matcher.group(1), formatter);
            } catch (Exception e) {
                log.warn("Failed to parse date: {}", matcher.group(1));
            }
        }
        return null;
    }
    
    private LocalTime extractIncidentTime(String text) {
        Pattern timePattern = Pattern.compile("(\\d{1,2}):(\\d{2})\\s*(AM|PM|am|pm)?");
        Matcher matcher = timePattern.matcher(text);
        if (matcher.find()) {
            try {
                int hour = Integer.parseInt(matcher.group(1));
                int minute = Integer.parseInt(matcher.group(2));
                return LocalTime.of(hour, minute);
            } catch (Exception e) {
                log.warn("Failed to parse time");
            }
        }
        return null;
    }
    
    private String extractLocation(String text) {
        Pattern locationPattern = Pattern.compile(
            "(?:Location|Place|Address|Incident Occurred At)[:\\s]+([^.\\n]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = locationPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    
    private String extractDescription(String text) {
        Pattern descPattern = Pattern.compile(
            "(?:Description|Details|What Happened|Incident Description)[:\\s]+([^.]*?\\.)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = descPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        // If not found, return first 200 chars as description
        return text.substring(0, Math.min(200, text.length()));
    }
    
    private String extractClaimant(String text) {
        Pattern claimantPattern = Pattern.compile(
            "(?:Claimant|Insured|Policyholder)[:\\s]+([^\\n]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = claimantPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    
    private List<String> extractThirdParties(String text) {
        List<String> thirdParties = new ArrayList<>();
        Pattern thirdPartyPattern = Pattern.compile(
            "(?:Third Party|Other Party|At-fault Party)[:\\s]*([^\\n]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = thirdPartyPattern.matcher(text);
        while (matcher.find()) {
            thirdParties.add(matcher.group(1).trim());
        }
        return thirdParties.isEmpty() ? null : thirdParties;
    }
    
    private ContactDetails extractContactDetails(String text) {
        ContactDetails details = new ContactDetails();
        
        Matcher emailMatcher = EMAIL_PATTERN.matcher(text);
        if (emailMatcher.find()) {
            details.setEmail(emailMatcher.group());
        }
        
        Matcher phoneMatcher = PHONE_PATTERN.matcher(text);
        if (phoneMatcher.find()) {
            details.setPhoneNumber(phoneMatcher.group());
        }
        
        Pattern addressPattern = Pattern.compile(
            "(?:Address|Street)[:\\s]*([^\\n]+)"
        );
        Matcher addressMatcher = addressPattern.matcher(text);
        if (addressMatcher.find()) {
            details.setAddress(addressMatcher.group(1).trim());
        }
        
        return (details.getEmail() != null || details.getPhoneNumber() != null) ? details : null;
    }
    
    private String extractAssetType(String text) {
        Pattern assetPattern = Pattern.compile(
            "(?:Asset Type|Vehicle Type|Property Type)[:\\s]*([^\\n]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = assetPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    
    private String extractAssetId(String text) {
        Pattern assetIdPattern = Pattern.compile(
            "(?:Asset ID|VIN|Serial Number)[:\\s]*([A-Z0-9\\-]{5,20})",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = assetIdPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
    
    private BigDecimal extractEstimatedDamage(String text) {
        Pattern damagePattern = Pattern.compile(
            "(?:Estimated Damage|Damage Amount|Loss Amount)[:\\s]*\\$?([0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{2})?)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = damagePattern.matcher(text);
        if (matcher.find()) {
            try {
                String amount = matcher.group(1).replaceAll(",", "");
                return new BigDecimal(amount);
            } catch (Exception e) {
                log.warn("Failed to parse damage amount");
            }
        }
        return null;
    }
    
    private String extractClaimType(String text) {
        String lowerText = text.toLowerCase();
        
        if (lowerText.contains("injury") || lowerText.contains("bodily harm")) {
            return "INJURY";
        } else if (lowerText.contains("vehicle") || lowerText.contains("auto")) {
            return "AUTO";
        } else if (lowerText.contains("property") || lowerText.contains("home")) {
            return "PROPERTY";
        } else if (lowerText.contains("collision")) {
            return "COLLISION";
        } else {
            return "GENERAL";
        }
    }
    
    private BigDecimal extractInitialEstimate(String text) {
        return extractEstimatedDamage(text);
    }
    
    private String extractPolicyholder(String text) {
        Pattern namePattern = Pattern.compile(
            "(?:Policyholder|Insured|Name)[:\\s]*([A-Z][a-z]+\\s+[A-Z][a-z]+)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = namePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }
}