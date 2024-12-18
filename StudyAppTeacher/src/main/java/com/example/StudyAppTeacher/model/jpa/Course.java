package com.example.StudyAppTeacher.model.jpa;


import com.example.StudyAppTeacher.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    private String courseCode;
    private String courseName;
    private String description;
    private DifficultyLevel difficultyLevel;
    private List<Long> prerequisiteCourseIds;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Teacher teacher;
    private List<String> moduleIds;
}

