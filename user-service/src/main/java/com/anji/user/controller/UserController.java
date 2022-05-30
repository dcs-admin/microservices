package com.anji.user.controller;

import com.anji.user.entity.User;
import com.anji.user.repository.UserRepository;
import com.anji.user.service.UserService;
import com.anji.user.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/")
    public User saveUser(@RequestBody User user){
        log.info("Post:saveUser:"+user);
        return this.userService.saveUser(user);
    }


    @GetMapping("/{userId}")
    public ResponseVO findUserWithDepratment(@PathVariable long userId) throws Exception {
        log.info("Get:findUserId:"+userId);
        return this.userService.findUserWithDepratment(userId);

    }

}
