package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthCheckController {

    @GetMapping("/check")
    public String hello() {
        return "Yes, I am authenticated";
    }

}
