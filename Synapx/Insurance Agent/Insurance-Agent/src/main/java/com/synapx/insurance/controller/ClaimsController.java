package com.synapx.insurance.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synapx.insurance.model.ClaimsResponse;
import com.synapx.insurance.model.FNOLRequest;
import com.synapx.insurance.service.ClaimsExtractionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/claims")
@Slf4j
public class ClaimsController {
    
    @Autowired
    private ClaimsExtractionService claimsExtractionService;
    
    @PostMapping("/process")
    public ResponseEntity<ClaimsResponse> processClaim(@RequestBody FNOLRequest request) {
        try {
            ClaimsResponse response = claimsExtractionService.processClaimDocument(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in claims processing", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Claims Processing Agent is running"));
    }
}