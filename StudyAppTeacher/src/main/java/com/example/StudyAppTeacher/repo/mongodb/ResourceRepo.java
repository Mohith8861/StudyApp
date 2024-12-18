package com.example.StudyAppTeacher.repo.mongodb;

import com.example.StudyAppTeacher.model.mongodb.Resource;
import com.example.StudyAppTeacher.enums.ResourceType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepo extends MongoRepository<Resource, String> {
    List<Resource> findByResourceType(ResourceType type);
    List<Resource> findByTopicId(String id);

}
