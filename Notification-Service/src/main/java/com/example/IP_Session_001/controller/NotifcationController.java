package com.example.IP_Session_001.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotifcationController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from NotificationController!";
    }
}
