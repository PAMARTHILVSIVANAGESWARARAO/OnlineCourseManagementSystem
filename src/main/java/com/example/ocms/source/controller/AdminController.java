package com.example.ocms.source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ocms.source.io.CourseDto;
import com.example.ocms.source.service.AuthService;
import com.example.ocms.source.service.PlatformService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AuthService auth;
    @Autowired
    private PlatformService platform;

    private void admin(String token) {
        if (!"ADMIN".equals(auth.current(token).getRole()))
            throw new IllegalArgumentException("Admin role required");
    }

    @GetMapping("/instructors/pending")
    public Object pending(@RequestHeader("Authorization") String t) {
        admin(t);
        return platform.pending();
    }

    @PutMapping("/instructors/{id}/verify")
    public Object verify(@RequestHeader("Authorization") String t, @PathVariable Long id) {
        admin(t);
        platform.verify(id);
        return "verified";
    }

    @GetMapping("/courses/{id}/enrollments")
    public Object enrollments(@RequestHeader("Authorization") String t, @PathVariable Long id) {
        admin(t);
        return platform.enrollments(id);
    }

    @PostMapping("/courses")
    public Object createCourse(@RequestHeader("Authorization") String t, @RequestBody CourseDto d,
            @RequestParam(required = false) Long instructorId) {
        admin(t);
        return platform.createForAdmin(d, instructorId);
    }
}
