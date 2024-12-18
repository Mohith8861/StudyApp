package com.example.StudyAppTeacher.repo.jpa;

import com.example.StudyAppTeacher.model.jpa.Course;
import com.example.StudyAppTeacher.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    List<Course> findByDifficultyLevel(DifficultyLevel level);
    List<Course> findByIsActiveTrue();
}

