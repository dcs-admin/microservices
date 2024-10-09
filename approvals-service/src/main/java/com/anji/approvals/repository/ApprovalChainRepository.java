package com.anji.approvals.repository;

import com.anji.approvals.entity.Approval;
import com.anji.approvals.entity.ApprovalChain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalChainRepository extends JpaRepository<ApprovalChain, Long> {
}
