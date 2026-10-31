package com.example.ocms.source.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ocms.source.model.CourseMaterial;
@Repository 
public interface MaterialRepository extends JpaRepository<CourseMaterial, Long> {
    List<CourseMaterial> findByCreatedAtAfter(java.time.LocalDateTime time);
}
