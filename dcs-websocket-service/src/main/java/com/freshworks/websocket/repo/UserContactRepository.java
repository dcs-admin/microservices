
package com.freshworks.websocket.repo;

 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.freshworks.websocket.model.UserContact; 

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    
    public List<UserContact> findByUserId(Long userId);
}