package com.example.StudyAppTeacher.repo.mongodb;

import com.example.StudyAppTeacher.model.mongodb.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepo extends MongoRepository<Topic, String> {
    List<Topic> findByModuleId(String id);
    List<Topic> findBySequenceOrderBetween(Integer start, Integer end);
}
