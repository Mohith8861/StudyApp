package com.example.StudyAppTeacher.service;

import com.example.StudyAppTeacher.dto.ModuleDTO;
import com.example.StudyAppTeacher.dto.ResourceDTO;
import com.example.StudyAppTeacher.dto.TopicDTO;
import com.example.StudyAppTeacher.model.jpa.Course;
import com.example.StudyAppTeacher.model.mongodb.Resource;
import com.example.StudyAppTeacher.model.mongodb.Topic;
import com.example.StudyAppTeacher.repo.jpa.CourseRepo;
import com.example.StudyAppTeacher.repo.mongodb.ModuleRepo;
import com.example.StudyAppTeacher.model.mongodb.Module;
import com.example.StudyAppTeacher.repo.mongodb.ResourceRepo;
import com.example.StudyAppTeacher.repo.mongodb.TopicRepo;
import com.example.StudyAppTeacher.utilities.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    private final ModuleRepo moduleRepo;
    private final TopicRepo topicRepo;
    private final CourseRepo courseRepo;
    private final ResourceRepo resourceRepo;
    private final Mapper mapper;

    @Autowired
    public ModuleService(ModuleRepo moduleRepo, TopicRepo topicRepo,
                         CourseRepo courseRepo, ResourceRepo resourceRepo,
                         Mapper mapper) {
        this.moduleRepo = moduleRepo;
        this.topicRepo = topicRepo;
        this.courseRepo = courseRepo;
        this.resourceRepo = resourceRepo;
        this.mapper = mapper;
    }

    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        Module module = moduleRepo.save(mapper.toEntity(moduleDTO));
        module.setTopics(new ArrayList<Topic>());

        // Handle Topics
        if (moduleDTO.getTopics() != null && !moduleDTO.getTopics().isEmpty()) {
            List<Topic> topics  = new ArrayList<Topic>();
             for(TopicDTO topicDTO :moduleDTO.getTopics()){

                 Topic topic = topicRepo.save(mapper.toEntity(topicDTO));
                 topic.setResourceIds(new ArrayList<String>());
                 topic.setModuleId(module.getId());

                 if(topicDTO.getResources() != null && !topicDTO.getResources().isEmpty())
                 {
                     for (ResourceDTO resourceDTO: topicDTO.getResources()){
                         resourceDTO.setTopicId(topic.getId());
                         Resource resource = resourceRepo.save(mapper.toEntity(resourceDTO));
                         topic.getResourceIds().add(resource.getId());
                     }
                 }

                 topic = topicRepo.save(topic);
                 module.getTopics().add(topic);
             }
        }

        Module savedModule = moduleRepo.save(module);

        // Update Course with Module ID
        if (module.getCourseId() != null) {
            Course course = courseRepo.findById(module.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));

            if (course.getModuleIds() == null) {
                course.setModuleIds(new ArrayList<>());
            }
            course.getModuleIds().add(savedModule.getId());
            courseRepo.save(course);
        }

        return mapper.toDTO(savedModule);
    }

    public ModuleDTO updateModule(String moduleId, ModuleDTO moduleDTO) {
        Module existingModule = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));

        existingModule.setModuleName(moduleDTO.getModuleName());
        existingModule.setModuleDescription(moduleDTO.getModuleDescription());
        existingModule.setSequenceOrder(moduleDTO.getSequenceOrder());
        existingModule.setDurationHours(moduleDTO.getDurationHours());
        existingModule.setDurationMinutes(moduleDTO.getDurationMinutes());
        existingModule.setCourseId(moduleDTO.getCourseId());

        // Update Topics
        if (moduleDTO.getTopics() != null) {
            // Remove existing topics
            if (existingModule.getTopics() != null) {
                topicRepo.deleteAll(existingModule.getTopics());
            }

            // Save new topics
            List<Topic> updatedTopics = moduleDTO.getTopics().stream()
                    .map(topicDTO -> {
                        Topic topic = mapper.toEntity(topicDTO);
                        return topicRepo.save(topic);
                    })
                    .collect(Collectors.toList());

            existingModule.setTopics(updatedTopics);
        }

        Module updatedModule = moduleRepo.save(existingModule);
        return mapper.toDTO(updatedModule);
    }

    public ModuleDTO getModuleById(String moduleId) {
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));
        return mapper.toDTO(module);
    }

    public void deleteModule(String moduleId) {
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found"));

        // Remove module from course
        if (module.getCourseId() != null) {
            Course course = courseRepo.findById(module.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));

            if (course.getModuleIds() != null) {
                course.getModuleIds().remove(moduleId);
                courseRepo.save(course);
            }
        }

        // Delete associated topics
        if (module.getTopics() != null) {
            topicRepo.deleteAll(module.getTopics());
        }

        moduleRepo.delete(module);
    }

    public List<ModuleDTO> getModulesByCourseId(Long courseId) {
        List<Module> modules = moduleRepo.findByCourseId(courseId);
        return modules.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ModuleDTO> getAllModules(){
        List<Module> modules = moduleRepo.findAll();
        return modules.stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}