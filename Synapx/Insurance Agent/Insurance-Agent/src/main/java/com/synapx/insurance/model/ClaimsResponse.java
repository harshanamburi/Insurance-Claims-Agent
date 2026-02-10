package com.synapx.insurance.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimsResponse {
    
    @JsonProperty("extractedFields")
    private ExtractedFields extractedFields;
    
    @JsonProperty("missingFields")
    private List<String> missingFields;
    
    @JsonProperty("recommendedRoute")
    private String recommendedRoute;
    
    @JsonProperty("reasoning")
    private String reasoning;
    
    @JsonProperty("flags")
    private List<String> flags;
    
    @JsonProperty("claimId")
    private String claimId;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}