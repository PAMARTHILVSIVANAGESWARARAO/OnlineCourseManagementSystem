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
import com.example.ocms.source.io.CourseResponseDto;
import com.example.ocms.source.io.EnrollmentResponseDto;
import com.example.ocms.source.io.ResponseMapper;
import com.example.ocms.source.io.UserResponseDto;
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
    public java.util.List<UserResponseDto> pending(@RequestHeader("Authorization") String t) {
        admin(t);
        return platform.pending().stream().map(ResponseMapper::user).toList();
    }

    @PutMapping("/instructors/{id}/verify")
    public Object verify(@RequestHeader("Authorization") String t, @PathVariable Long id) {
        admin(t);
        platform.verify(id);
        return "verified";
    }

    @GetMapping("/courses/{id}/enrollments")
    public java.util.List<EnrollmentResponseDto> enrollments(@RequestHeader("Authorization") String t, @PathVariable Long id) {
        admin(t);
        return platform.enrollments(id).stream().map(ResponseMapper::enrollment).toList();
    }

    @PostMapping("/courses")
    public CourseResponseDto createCourse(@RequestHeader("Authorization") String t, @RequestBody CourseDto d,
            @RequestParam(required = false) Long instructorId) {
        admin(t);
        return ResponseMapper.course(platform.createForAdmin(d, instructorId));
    }
}
