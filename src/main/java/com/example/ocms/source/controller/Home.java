package com.example.ocms.source.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Home {
    @GetMapping
    public Map<String, String> home() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("application", "OCMS");
        result.put("status", "running");
        return result;
    }
}
