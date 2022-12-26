
package com.freshworks.websocket.repo;

 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freshworks.websocket.model.UserMessage;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    List<UserMessage> findByFromUserOrToUserOrFromUserOrToUserOrderByCreatedDateDesc(Long fromUser, Long toUser, Long fromUser2, Long toUser2);

 

}