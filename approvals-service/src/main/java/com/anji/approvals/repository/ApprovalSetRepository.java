package com.anji.approvals.repository;

import com.anji.approvals.entity.Approval;
import com.anji.approvals.entity.ApprovalSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalSetRepository extends JpaRepository<ApprovalSet, Long> {
}
