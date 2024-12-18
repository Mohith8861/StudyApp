package com.example.StudyAppTeacher.repo.mongodb;

import com.example.StudyAppTeacher.model.mongodb.Module;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepo extends MongoRepository<Module, String> {
    List<Module> findByCourseId(Long id);
    List<Module> findBySequenceOrderBetween(Integer start, Integer end);
}
