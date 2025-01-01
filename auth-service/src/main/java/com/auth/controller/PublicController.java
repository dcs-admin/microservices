package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication/api/v1")
public class PublicController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
