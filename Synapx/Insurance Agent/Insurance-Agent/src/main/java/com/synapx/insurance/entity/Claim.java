package com.synapx.insurance.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claims")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Claim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String claimId;
    
    private String policyNumber;
    private String claimantName;
    private String claimType;
    private BigDecimal estimatedDamage;
    private String routingDecision;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}