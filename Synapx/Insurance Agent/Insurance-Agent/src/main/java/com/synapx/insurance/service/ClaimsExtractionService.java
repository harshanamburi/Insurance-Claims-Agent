package com.synapx.insurance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synapx.insurance.entity.Claim;
import com.synapx.insurance.model.ClaimsResponse;
import com.synapx.insurance.model.ExtractedFields;
import com.synapx.insurance.model.FNOLRequest;
import com.synapx.insurance.repository.ClaimRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClaimsExtractionService {
    
    @Autowired
    private DocumentParsingService documentParsingService;
    
    @Autowired
    private FieldExtractor fieldExtractor;
    
    @Autowired
    private FieldValidationService validationService;
    
    @Autowired
    private RoutingService routingService;
    
    @Autowired
    private ClaimRepository claimRepository;
    
    public ClaimsResponse processClaimDocument(FNOLRequest request) {
        try {
            // Step 1: Parse document
            log.info("Processing document: {}", request.getFileName());
            String documentText = documentParsingService.parseDocument(request);
            
            // Step 2: Extract fields
            ExtractedFields fields = fieldExtractor.extractFields(documentText);
            
            // Step 3: Validate fields
            List<String> missingFields = validationService.validateFields(fields);
            
            // Step 4: Route claim
            ClaimsResponse response = routingService.routeClaim(fields, missingFields);
            
            // Step 5: Generate claim ID
            String claimId = generateClaimId();
            response.setClaimId(claimId);
            
            // Step 6: Save to database
            saveClaim(claimId, fields, response);
            
            log.info("Claim processed successfully: {}", claimId);
            return response;
            
        } catch (Exception e) {
            log.error("Error processing claim document", e);
            throw new RuntimeException("Failed to process claim: " + e.getMessage());
        }
    }
    
    private String generateClaimId() {
        return "CLM-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    private void saveClaim(String claimId, ExtractedFields fields, ClaimsResponse response) {
        try {
            Claim claim = new Claim();
            claim.setClaimId(claimId);
            claim.setPolicyNumber(fields.getPolicyNumber());
            claim.setClaimantName(fields.getClaimant());
            claim.setClaimType(fields.getClaimType());
            claim.setEstimatedDamage(fields.getEstimatedDamage());
            claim.setRoutingDecision(response.getRecommendedRoute());
            claim.setStatus("PROCESSED");
            claim.setCreatedAt(LocalDateTime.now());
            
            claimRepository.save(claim);
            log.info("Claim saved with ID: {}", claimId);
        } catch (Exception e) {
            log.warn("Failed to save claim to database", e);
        }
    }
}