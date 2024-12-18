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
@Document(collection = "module")
public class Module {
    @MongoId
    private String id;
    private String moduleName;
    private String moduleDescription;
    private Integer sequenceOrder;
    private Integer durationHours;
    private Integer durationMinutes;
    private Long courseId;
    private List<Topic> topics;
}
