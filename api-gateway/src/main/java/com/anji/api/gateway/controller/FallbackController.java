package com.anji.api.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class FallbackController {

    @GetMapping("/userServiceFallback")
    public String userServicefallback(){
        return "USER-SERVICE is not responding ontime, please try again later";
    }


    @GetMapping("/departmentServiceFallback")
    public String departmentServicefallback(){
        return "DEPARTMENT-SERVICE is not responding ontime, please try again later";
    }
}
