package com.example.ocms.source.io;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnrollmentResponseDto {
    private Long id;
    private LocalDateTime enrolledAt;
    private int progress;
    private CourseResponseDto course;
    private UserResponseDto student;
}