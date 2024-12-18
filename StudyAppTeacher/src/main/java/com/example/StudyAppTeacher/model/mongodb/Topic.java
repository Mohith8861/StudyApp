package com.example.StudyAppTeacher.model.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "topic")
public class Topic {

    @MongoId
    private String id;
    private String topicName;
    private String topicDescription;
    private Integer sequenceOrder;
    private String moduleId;
    private List<String> resourceIds;
}
