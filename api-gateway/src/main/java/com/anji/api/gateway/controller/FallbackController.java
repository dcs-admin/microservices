package com.anji.api.gateway.controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FallbackController {

    @GetMapping("/customerServiceFallback")
    public String customerServicefallback(){
        return "CUSTOMER-SERVICE is not responding ontime, please try again later";
    }


    @GetMapping("/productServiceFallback")
    public String productServicefallback(){
        return "PRODUCT-SERVICE is not responding ontime, please try again later";
    }

    @GetMapping("/orderServiceFallback")
    public String orderServicefallback(){
        return "ORDER-SERVICE is not responding ontime, please try again later";
    }

    @GetMapping("/websocketServiceFallback")
    public String websocketServicefallback(){
        return "WEBSOCKET-SERVICE is not responding ontime, please try again later";
    }



    @GetMapping("/authenticationServiceFallback")
    public String authenticationServiceFallback(){
        return "AuthenticationService is not responding ontime, please try again later";
    }


}
