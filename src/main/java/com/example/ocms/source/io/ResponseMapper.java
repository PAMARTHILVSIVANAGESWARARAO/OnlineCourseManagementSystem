package com.example.ocms.source.io;

import java.util.List;
import java.util.stream.Collectors;

import com.example.ocms.source.model.Course;
import com.example.ocms.source.model.CourseMaterial;
import com.example.ocms.source.model.Enrollment;
import com.example.ocms.source.model.User;

public final class ResponseMapper {
    private ResponseMapper() {
    }

    public static UserResponseDto user(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(user.getId(), user.getEmail(), user.getRole(), user.isNotVerified());
    }

    public static MaterialResponseDto material(CourseMaterial material) {
        return new MaterialResponseDto(material.getId(), material.getTitle(), material.getLink(), material.getCreatedAt());
    }

    public static CourseResponseDto course(Course course) {
        List<MaterialResponseDto> materials = course.getMaterials().stream()
                .map(ResponseMapper::material)
                .collect(Collectors.toList());
        List<EnrollmentResponseDto> enrollments = course.getEnrollments().stream()
            .map(ResponseMapper::enrollment)
            .collect(Collectors.toList());
        return new CourseResponseDto(course.getId(), course.getTitle(), course.getDescription(), course.getCategory(),
            course.getCreatedAt(), user(course.getInstructor()), materials, enrollments);
    }

    public static EnrollmentResponseDto enrollment(Enrollment enrollment) {
        return new EnrollmentResponseDto(enrollment.getId(), enrollment.getEnrolledAt(), enrollment.getProgress(),
                courseSummary(enrollment.getCourse()), user(enrollment.getStudent()));
    }

    private static CourseResponseDto courseSummary(Course course) {
        return new CourseResponseDto(course.getId(), course.getTitle(), course.getDescription(), course.getCategory(),
            course.getCreatedAt(), user(course.getInstructor()), List.of(), List.of());
    }
}