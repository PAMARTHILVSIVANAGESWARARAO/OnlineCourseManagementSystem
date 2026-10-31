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
import com.example.ocms.source.io.MaterialDto;
import com.example.ocms.source.io.MaterialResponseDto;
import com.example.ocms.source.io.ProgressDto;
import com.example.ocms.source.io.ResponseMapper;
import com.example.ocms.source.model.User;
import com.example.ocms.source.service.AuthService;
import com.example.ocms.source.service.PlatformService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private AuthService auth;
    @Autowired
    private PlatformService platform;

    @GetMapping
    public java.util.List<CourseResponseDto> list(@RequestParam(required = false) String search) {
        return platform.search(search).stream().map(ResponseMapper::course).toList();
    }

    @GetMapping("/notifications/new")
    public java.util.List<CourseResponseDto> newCourses() {
        return platform.recentCourses().stream().map(ResponseMapper::course).toList();
    }

    @GetMapping("/notifications/materials")
    public java.util.List<MaterialResponseDto> newMaterials() {
        return platform.recentMaterials().stream().map(ResponseMapper::material).toList();
    }

    @GetMapping("/{id}/materials")
    public java.util.List<MaterialResponseDto> materials(@PathVariable Long id, @RequestHeader("Authorization") String t) {
        User u = auth.current(t);
        if ("STUDENT".equals(u.getRole()) && !platform.enrolled(id, u))
            throw new IllegalArgumentException("Enroll before accessing materials");
        return platform.materials(id).stream().map(ResponseMapper::material).toList();
    }

    @PostMapping
    public CourseResponseDto create(@RequestHeader("Authorization") String t, @RequestBody CourseDto d) {
        User u = auth.current(t);
        if (!"ADMIN".equals(u.getRole()))
            throw new IllegalArgumentException("Admin role required");
        return ResponseMapper.course(platform.createForAdmin(d, null));
    }

    @PostMapping("/{id}/materials")
    public MaterialResponseDto material(@RequestHeader("Authorization") String t, @PathVariable Long id,
            @RequestBody MaterialDto d) {
        return ResponseMapper.material(platform.material(id, d, auth.current(t)));
    }

    @PostMapping("/{id}/enroll")
    public com.example.ocms.source.io.EnrollmentResponseDto enroll(@RequestHeader("Authorization") String t, @PathVariable Long id) {
        User u = auth.current(t);
        if (!"STUDENT".equals(u.getRole()))
            throw new IllegalArgumentException("Student role required");
        return ResponseMapper.enrollment(platform.enroll(id, u));
    }

    @PutMapping("/{id}/progress")
    public com.example.ocms.source.io.EnrollmentResponseDto progress(@RequestHeader("Authorization") String t, @PathVariable Long id,
            @RequestBody ProgressDto d) {
        return ResponseMapper.enrollment(platform.progress(id, d.getProgress(), auth.current(t)));
    }
}
