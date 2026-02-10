package com.synapx.insurance.model;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FNOLRequest {
    
    @NotNull
    private String documentContent;
    
    @NotNull
    private String documentType; // "PDF" or "TXT"
    
    private String fileName;
    
    @NotNull
    private String documentFormat;
}