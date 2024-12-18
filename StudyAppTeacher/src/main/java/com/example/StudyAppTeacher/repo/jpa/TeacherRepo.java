package com.example.StudyAppTeacher.repo.jpa;

import com.example.StudyAppTeacher.model.jpa.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    List<Teacher> findBySpecialization(String specialization);
    List<Teacher> findByIsActiveTrue();
    Optional<Teacher> findByEmail(String email);
}
