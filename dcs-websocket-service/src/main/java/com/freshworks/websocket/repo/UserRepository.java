
package com.freshworks.websocket.repo;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freshworks.websocket.model.User; 

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUserNumber(Long userNumber);

}