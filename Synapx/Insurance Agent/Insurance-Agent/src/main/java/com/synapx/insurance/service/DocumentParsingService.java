package com.synapx.insurance.service;

import java.io.IOException;
import java.util.Base64;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.synapx.insurance.model.FNOLRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentParsingService {
    
    public String parseDocument(FNOLRequest request) throws IOException {
        if ("PDF".equalsIgnoreCase(request.getDocumentType())) {
            return parsePDF(request.getDocumentContent());
        } else if ("TXT".equalsIgnoreCase(request.getDocumentType())) {
            return parseTXT(request.getDocumentContent());
        } else {
            throw new IllegalArgumentException("Unsupported document type: " + request.getDocumentType());
        }
    }
    
    private String parsePDF(String base64Content) throws IOException {
        try {
            // Decode Base64 to byte array
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            
            // Pass byte[] directly to loadPDF ✅
            try (PDDocument document = Loader.loadPDF(decodedBytes)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                
                log.info("Extracted text from PDF, length: {}", text.length());
                return text;
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid Base64 encoding", e);
            throw new RuntimeException("Invalid Base64 encoding", e);
        } catch (IOException e) {
            log.error("Error parsing PDF", e);
            throw new RuntimeException("Failed to parse PDF: " + e.getMessage(), e);
        }
    }
    
    private String parseTXT(String content) {
        log.info("Parsed TXT document, length: {}", content.length());
        return content;
    }
}