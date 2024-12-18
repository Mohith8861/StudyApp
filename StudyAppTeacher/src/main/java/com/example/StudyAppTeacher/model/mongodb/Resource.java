package com.example.StudyAppTeacher.model.mongodb;

import com.example.StudyAppTeacher.enums.ResourceType;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resource")
public class Resource {

    @MongoId
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
