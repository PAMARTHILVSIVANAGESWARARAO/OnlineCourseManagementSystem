package com.example.ocms.source.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ocms.source.io.CourseDto;
import com.example.ocms.source.io.MaterialDto;
import com.example.ocms.source.model.Course;
import com.example.ocms.source.model.CourseMaterial;
import com.example.ocms.source.model.Enrollment;
import com.example.ocms.source.model.User;
import com.example.ocms.source.repository.CourseRepository;
import com.example.ocms.source.repository.EnrollmentRepository;
import com.example.ocms.source.repository.MaterialRepository;
import com.example.ocms.source.repository.UserRepository;

@Service
public class PlatformService {
    @Autowired
    private CourseRepository courses;
    @Autowired
    private MaterialRepository materials;
    @Autowired
    private EnrollmentRepository enrollments;
    @Autowired
    private UserRepository users;

    public Course create(CourseDto d, User instructor) {
        Course c = new Course();
        c.setTitle(d.getTitle());
        c.setDescription(d.getDescription());
        c.setCategory(d.getCategory());
        c.setInstructor(instructor);
        return courses.save(c);
    }

    public Course createForAdmin(CourseDto d, Long instructorId) {
        Course c = new Course();
        c.setTitle(d.getTitle());
        c.setDescription(d.getDescription());
        c.setCategory(d.getCategory());
        if (instructorId != null) {
            Optional<User> f = users.findById(instructorId);
            if (!f.isPresent())
                throw new IllegalArgumentException("Instructor not found");
            c.setInstructor(f.get());
        }
        return courses.save(c);
    }

    public CourseMaterial material(Long id, MaterialDto d, User instructor) {
        Course c = course(id);
        if (!"INSTRUCTOR".equals(instructor.getRole()) || instructor.isNotVerified())
            throw new IllegalArgumentException("Verified instructor required");
        CourseMaterial m = new CourseMaterial();
        m.setTitle(d.getTitle());
        m.setLink(d.getLink());
        m.setCourse(c);
        return materials.save(m);
    }

    public Enrollment enroll(Long id, User student) {
        Course c = course(id);
        if (enrollments.findByStudentIdAndCourseId(student.getId(), id).isPresent())
            throw new IllegalArgumentException("Already enrolled");
        Enrollment e = new Enrollment();
        e.setCourse(c);
        e.setStudent(student);
        return enrollments.save(e);
    }

    public List<Course> recentCourses() {
        return courses.findByCreatedAtAfter(LocalDateTime.now().minusHours(24));
    }

    public List<CourseMaterial> recentMaterials() {
        return materials.findByCreatedAtAfter(LocalDateTime.now().minusDays(3));
    }

    public List<Course> search(String q) {
        if (q == null || q.trim().isEmpty())
            return courses.findAll();
        return courses.findByTitleContainingIgnoreCase(q);
    }

    public void verify(Long id) {
        Optional<User> f = users.findById(id);
        if (!f.isPresent())
            throw new IllegalArgumentException("User not found");
        User u = f.get();
        if (!"INSTRUCTOR".equals(u.getRole()))
            throw new IllegalArgumentException("Not an instructor");
        u.setNotVerified(false);
        users.save(u);
    }

    public List<User> pending() {
        return users.findByRoleAndNotVerified("INSTRUCTOR", true);
    }

    public List<Enrollment> enrollments(Long courseId) {
        return enrollments.findByCourseId(courseId);
    }

    public List<CourseMaterial> materials(Long courseId) {
        return course(courseId).getMaterials();
    }

    public boolean enrolled(Long courseId, User student) {
        return enrollments.findByStudentIdAndCourseId(student.getId(), courseId).isPresent();
    }

    public Enrollment progress(Long courseId, int value, User student) {
        Optional<Enrollment> f = enrollments.findByStudentIdAndCourseId(student.getId(), courseId);
        if (!f.isPresent())
            throw new IllegalArgumentException("Not enrolled");
        Enrollment e = f.get();
        e.setProgress(Math.max(0, Math.min(100, value)));
        return enrollments.save(e);
    }

    public List<Enrollment> myEnrollments(User student) {
        return enrollments.findByStudentId(student.getId());
    }

    private Course course(Long id) {
        Optional<Course> f = courses.findById(id);
        if (!f.isPresent())
            throw new IllegalArgumentException("Course not found");
        return f.get();
    }
}
