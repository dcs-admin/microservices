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
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long accountId;
    private long approvableId;
    private String approvableType; 
    private long approvalSetId; 
    private long  approvalPolicyId;
    private long  userId;
    private int  memberId;    
    private int  delegateeId; 
    private int status; 
    private Date  triggeredAt;
    private String  token;
    private boolean  deleted;
    private String  remark;
    private Date  lastRemindedAt;
    private long  existsingApprovalId; 
    private Date createdAt; 
    private Date updatedAt; 



}
