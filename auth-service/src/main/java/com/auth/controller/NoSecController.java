package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication/api/public")
public class NoSecController {

    @GetMapping("/hello")
    public String hello() {
        return "{ 'c1': 'Hello, World', 'c2': 'Hello, GPT'}";
    }

}
