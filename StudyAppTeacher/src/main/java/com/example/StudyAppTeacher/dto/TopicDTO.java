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
public class TopicDTO {

    private String id;
    private String topicName;
    private String topicDescription;
    private Integer sequenceOrder;
    private String moduleId;
    private List<String> resourceIds;
    private List<ResourceDTO> resources;
}
