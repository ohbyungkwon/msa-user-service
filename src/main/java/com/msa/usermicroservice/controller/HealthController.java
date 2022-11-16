package com.msa.usermicroservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @Value("${spring.application.name}")
    private String server;

    @Value("${server.port}")
    private String port;

    @Value("${welcome.message}")
    private String message;

    @GetMapping("/health")
    public String checkHealth() {
        return String.format("health is ok, This is %s:$s", server, port);
    }

    @GetMapping("/welcome")
    public String welcome() {
        return this.message;
    }
}
