package com.example.demo.config.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class DemoController {

    @GetMapping("/security-test")
    public ResponseEntity<String> securityTest() {
        return ResponseEntity.ok("Response from secure endpoint..");
    }
}