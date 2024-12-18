package com.example.StudyAppTeacher.dto;

import com.example.StudyAppTeacher.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    private String id;
    private ResourceType resourceType;
    private String name;
    private String description;
    private String resourceUrl;
    private Long fileSize;
    private LocalDateTime uploadDate;
    private String topicId;
//    private String topicName;
}
