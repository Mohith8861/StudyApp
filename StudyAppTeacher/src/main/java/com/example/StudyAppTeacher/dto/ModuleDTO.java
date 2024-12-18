package com.example.StudyAppTeacher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDTO {
    private String id;
    private String moduleName;
    private String moduleDescription;
    private Integer sequenceOrder;
    private Integer durationHours;
    private Integer durationMinutes;
    private Long courseId;
    private List<TopicDTO> topics;
}
