package com.example.ocms.source.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ocms.source.io.AuthDto;
import com.example.ocms.source.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService auth;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody AuthDto d) {
        auth.register(d.getEmail(), d.getPassword(), d.getRole());
        Map<String, Object> r = new HashMap<String, Object>();
        r.put("message", "registered");
        return r;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody AuthDto d) {
        Map<String, Object> r = new HashMap<String, Object>();
        r.put("token", auth.login(d.getEmail(), d.getPassword()));
        return r;
    }
}
