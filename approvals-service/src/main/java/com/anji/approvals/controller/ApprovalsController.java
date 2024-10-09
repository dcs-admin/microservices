package com.anji.approvals.controller;

import com.anji.approvals.entity.Approval;
import com.anji.approvals.repository.ApprovalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/approvals")
@Slf4j
public class ApprovalsController {

    @Autowired
    private ApprovalRepository approvalRepository;


    @PostMapping("/")
    public Approval saveApproval(@RequestBody Approval approval){
        log.info("Post:saveApproval:"+approval);
        return this.approvalRepository.save(approval);
    }


    @GetMapping("/{approvalId}")
    public ResponseEntity<Approval> findApprovalId(@PathVariable long approvalId) {
        log.info("Get:findApprovalId:" + approvalId);
        Optional<Approval> optional = this.approvalRepository.findById(approvalId);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

}
