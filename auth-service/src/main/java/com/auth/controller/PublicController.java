package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping("/login-user")
    public ResponseEntity<String> bookTicket(@RequestParam String userId,
                                             @RequestParam String password) {

        return ResponseEntity.ok("Thanks");
    }
}
