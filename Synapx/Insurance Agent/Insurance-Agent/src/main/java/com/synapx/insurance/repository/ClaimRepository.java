package com.synapx.insurance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synapx.insurance.entity.Claim;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByClaimId(String claimId);
    List<Claim> findByRoutingDecision(String routingDecision);
    List<Claim> findByStatus(String status);
}
