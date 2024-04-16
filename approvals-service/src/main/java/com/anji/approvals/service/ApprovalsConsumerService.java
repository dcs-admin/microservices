package com.anji.approvals.service;

import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.approvals.entity.Approval;
import com.anji.approvals.entity.ApprovalChain;
import com.anji.approvals.entity.ApprovalSet;
import com.anji.approvals.repository.ApprovalChainRepository;
import com.anji.approvals.repository.ApprovalRepository;
import com.anji.approvals.repository.ApprovalSetRepository;

import java.util.function.Consumer;
import org.apache.kafka.connect.data.Struct;

@Service
public class ApprovalsConsumerService { 

   

    @Autowired
    private ApprovalChainRepository approvalChainRepository;

    @Autowired
    private ApprovalSetRepository approvalSetRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    public Consumer<SourceRecord> buildConsumer(){

        Consumer<SourceRecord> consumer = record -> {
            
            // Process the change event directly within your application
            System.out.println("Received event: " + record.key());

            //Maintain schema is schema proto file and process the fields accordingly here 
            try{
                
            Struct after = (Struct) ((Struct) record.value()).get("after");
           

            long id = after.getInt64("id");
            int levelId = (Short) after.get("level_id");
            

            System.out.println("\t\t after: " + after );
           
            if(levelId > 1){
                ApprovalChain approvalChain = new ApprovalChain();
                approvalChain.setAccountId(after.getInt64("account_id"));
                approvalChain.setId(id);
                approvalChain.setApprovableId(id);
                approvalChain.setApprovableType(null);
                approvalChain.setLevel(levelId+"");
                approvalChainRepository.save(approvalChain);

                ApprovalSet approvalSet = new ApprovalSet();
                approvalSet.setAccountId(after.getInt64("account_id"));
                approvalSet.setId(id); 
                approvalSetRepository.save(approvalSet);

                Approval approval = new Approval();
                approval.setId(id);
                approval.setAccountId(after.getInt64("account_id"));
                approval.setUserId( after.getInt64("user_id")); 
                approvalRepository.save(approval);
            } else {
                Approval approval = new Approval();
                approval.setId(id);
                approval.setAccountId(after.getInt64("account_id"));
                approval.setUserId( after.getInt64("user_id")); 
                approvalRepository.save(approval);
            }

        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Unable to process CDC Record: Missed one transaction :"+e.getMessage());
        }
            
        };

        

        return consumer;
    }
    
}
