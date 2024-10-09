package com.anji.approvals.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long accountId;
    private String name;
    private long approvalChainId;
    private long  approvalPolicyId;
    private String configType; 
    private int userId; 
    private int wfeventId; 
    private int status; 
    private Date createdAt; 
    private Date updatedAt; 


}
