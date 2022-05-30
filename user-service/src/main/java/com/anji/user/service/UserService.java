package com.anji.user.service;

import com.anji.user.entity.User;
import com.anji.user.repository.UserRepository;
import com.anji.user.vo.Department;
import com.anji.user.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
       return userRepository.save(user);
    }


    public ResponseVO findUserWithDepratment(long userId) throws Exception {

        log.info("In::findUserWithDepratment::"+userId);
        ResponseVO responseVO = new ResponseVO();
        User user = userRepository.findByUserId(userId);
        if(user != null) {
            Department department =
                    this.restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId(),
                            Department.class);
            responseVO.setUser(user);
            responseVO.setDepartment(department);
        }else{
            throw new Exception("Unable to find the user with this service");
        }

        log.info("Out::"+responseVO);
        return responseVO;
    }

}

