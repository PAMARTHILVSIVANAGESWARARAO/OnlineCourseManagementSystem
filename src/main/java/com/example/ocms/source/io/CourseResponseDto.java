package com.example.ocms.source.io;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseResponseDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime createdAt;
    private UserResponseDto instructor;
    private List<MaterialResponseDto> materials;
    private List<EnrollmentResponseDto> enrollments;
}