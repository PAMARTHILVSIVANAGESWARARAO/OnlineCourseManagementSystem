package com.example.ocms.source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ocms.source.service.AuthService;
import com.example.ocms.source.service.PlatformService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private AuthService auth;
    @Autowired
    private PlatformService platform;

    @GetMapping("/enrollments")
    public Object mine(@RequestHeader("Authorization") String t) {
        return platform.myEnrollments(auth.current(t));
    }
}
