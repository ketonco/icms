package com.icms.user_auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class TestController {
    
    // TODO (pending P-28)
    @GetMapping("/test")
    public String test() {
        return "User-Auth Service is running";
    }

}
