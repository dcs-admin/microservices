
package com.freshworks.websocket.repo;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.freshworks.websocket.model.UserLoginStatus; 

@Repository
public interface UserLoginStatusRepository extends JpaRepository<UserLoginStatus, Long> { 

}