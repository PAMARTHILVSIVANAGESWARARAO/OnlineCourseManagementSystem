package com.example.ocms.source.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ocms.source.model.Course;
@Repository 
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);

    List<Course> findByCreatedAtAfter(java.time.LocalDateTime time);
}
