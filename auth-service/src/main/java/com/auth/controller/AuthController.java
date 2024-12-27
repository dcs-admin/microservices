package com.auth.controller;

import com.auth.model.AuthRequest;
import com.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth")
    public  ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
                );
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            System.out.println("Authentication successful. Generating token...");
            String token = jwtUtil.generateToken(authRequest.getUserName());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
