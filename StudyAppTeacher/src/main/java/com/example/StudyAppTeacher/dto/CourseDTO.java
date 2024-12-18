package com.example.StudyAppTeacher.dto;


import com.example.StudyAppTeacher.enums.DifficultyLevel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long cid;
    private String courseCode;
    private String courseName;
    private String description;
    private DifficultyLevel difficultyLevel;
    private List<Long> prerequisiteCourseIds;
    private Boolean isActive;
    private Long teacherId;
    private List<String> moduleIds;
    private List<ModuleDTO> moduleDTOS;
}
