package com.anji.approvals.service;

import com.anji.approvals.repository.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ApprovalRepository departmentRepository;

}

