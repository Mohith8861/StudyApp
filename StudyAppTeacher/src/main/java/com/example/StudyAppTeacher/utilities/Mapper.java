package com.example.StudyAppTeacher.utilities;

import com.example.StudyAppTeacher.dto.*;
import com.example.StudyAppTeacher.model.jpa.Teacher;
import com.example.StudyAppTeacher.model.jpa.Course;
import com.example.StudyAppTeacher.model.mongodb.Module;
import com.example.StudyAppTeacher.model.mongodb.Resource;
import com.example.StudyAppTeacher.model.mongodb.Topic;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public TeacherDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;

        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setEmail(teacher.getEmail());
        dto.setPhoneNumber(teacher.getPhoneNumber());
        dto.setDateOfBirth(teacher.getDateOfBirth());
        dto.setGender(teacher.getGender());
        dto.setSpecialization(teacher.getSpecialization());
        dto.setIsActive(teacher.getIsActive());

        // Convert course IDs without repository dependency
        if (teacher.getCourses() != null) {
            dto.setCourseIds(teacher.getCourses().stream()
                    .map(Course::getCid)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Teacher toEntity(TeacherDTO dto) {
        if (dto == null) return null;

        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setEmail(dto.getEmail());
        teacher.setPhoneNumber(dto.getPhoneNumber());
        teacher.setDateOfBirth(dto.getDateOfBirth());
        teacher.setGender(dto.getGender());
        teacher.setSpecialization(dto.getSpecialization());
        teacher.setIsActive(dto.getIsActive());

        return teacher;
    }

    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setCid(course.getCid());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setDescription(course.getDescription());
        dto.setDifficultyLevel(course.getDifficultyLevel());
        dto.setPrerequisiteCourseIds(course.getPrerequisiteCourseIds());
        dto.setIsActive(course.getIsActive());
        dto.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);

        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;

        Course course = new Course();
        course.setCid((dto.getCid() != null) ? dto.getCid() : null);
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setDifficultyLevel(dto.getDifficultyLevel());
        course.setPrerequisiteCourseIds(dto.getPrerequisiteCourseIds());
        course.setIsActive(dto.getIsActive());

        return course;
    }

    public ModuleDTO toDTO(Module module) {
        if (module == null) return null;

        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setModuleName(module.getModuleName());
        dto.setModuleDescription(module.getModuleDescription());
        dto.setSequenceOrder(module.getSequenceOrder());
        dto.setDurationHours(module.getDurationHours());
        dto.setDurationMinutes(module.getDurationMinutes());
        dto.setCourseId(module.getCourseId());

        if (module.getTopics() != null) {
            dto.setTopics(module.getTopics().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Module toEntity(ModuleDTO dto) {
        if (dto == null) return null;

        Module module = new Module();
        module.setId(dto.getId());
        module.setModuleName(dto.getModuleName());
        module.setModuleDescription(dto.getModuleDescription());
        module.setSequenceOrder(dto.getSequenceOrder());
        module.setDurationHours(dto.getDurationHours());
        module.setDurationMinutes(dto.getDurationMinutes());
        module.setCourseId(dto.getCourseId());

        if (dto.getTopics() != null) {
            module.setTopics(dto.getTopics().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList()));
        }

        return module;
    }

    public TopicDTO toDTO(Topic topic) {
        if (topic == null) return null;

        TopicDTO dto = new TopicDTO();
        dto.setId(topic.getId());
        dto.setTopicName(topic.getTopicName());
        dto.setTopicDescription(topic.getTopicDescription());
        dto.setSequenceOrder(topic.getSequenceOrder());
        dto.setModuleId(topic.getModuleId());
        dto.setResourceIds(topic.getResourceIds());

        return dto;
    }

    public Topic toEntity(TopicDTO dto) {
        if (dto == null) return null;

        Topic topic = new Topic();
        topic.setId(dto.getId());
        topic.setTopicName(dto.getTopicName());
        topic.setTopicDescription(dto.getTopicDescription());
        topic.setSequenceOrder(dto.getSequenceOrder());
        topic.setModuleId(dto.getModuleId());
        topic.setResourceIds(dto.getResourceIds());

        return topic;
    }

    public ResourceDTO toDTO(Resource resource) {
        if (resource == null) return null;

        ResourceDTO dto = new ResourceDTO();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setDescription(resource.getDescription());
        dto.setResourceType(resource.getResourceType());
        dto.setResourceUrl(resource.getResourceUrl());
        dto.setFileSize(resource.getFileSize());
        dto.setUploadDate(resource.getUploadDate());
        dto.setTopicId(resource.getTopicId());

        return dto;
    }

    public Resource toEntity(ResourceDTO dto) {
        if (dto == null) return null;

        Resource resource = new Resource();
        resource.setId(dto.getId());
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setResourceType(dto.getResourceType());
        resource.setResourceUrl(dto.getResourceUrl());
        resource.setFileSize(dto.getFileSize());
        resource.setUploadDate(dto.getUploadDate());
        resource.setTopicId(dto.getTopicId());

        return resource;
    }
}