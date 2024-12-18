package com.example.StudyAppTeacher.service;

import com.example.StudyAppTeacher.dto.CourseDTO;
import com.example.StudyAppTeacher.dto.ModuleDTO;
import com.example.StudyAppTeacher.model.jpa.Teacher;
import com.example.StudyAppTeacher.model.jpa.Course;
import com.example.StudyAppTeacher.repo.jpa.CourseRepo;
import com.example.StudyAppTeacher.repo.jpa.TeacherRepo;
import com.example.StudyAppTeacher.repo.mongodb.ModuleRepo;
import com.example.StudyAppTeacher.utilities.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.StudyAppTeacher.model.mongodb.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final TeacherRepo teacherRepo;
    private final ModuleRepo moduleRepo;
    private final ModuleService moduleService;
    private final Mapper mapper;

    @Autowired
    public CourseService(CourseRepo courseRepo, TeacherRepo teacherRepo,
                         ModuleRepo moduleRepo, ModuleService moduleService, Mapper mapper) {
        this.courseRepo = courseRepo;
        this.teacherRepo = teacherRepo;
        this.moduleRepo = moduleRepo;
        this.moduleService = moduleService;
        this.mapper = mapper;
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Create Course
        Course course = mapper.toEntity(courseDTO);

        // Associate Teacher
        if (courseDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepo.findById(courseDTO.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
            course.setTeacher(teacher);
        }

        // Save Course first to get its ID
        Course savedCourse = courseRepo.save(course);
        Course finalCourse = savedCourse;
        // Handle Modules
        if (courseDTO.getModuleDTOS() != null && !courseDTO.getModuleDTOS().isEmpty()) {
            List<Module> modules = courseDTO.getModuleDTOS().stream()
                    .map(moduleDTO -> {
                        moduleDTO.setCourseId(savedCourse.getCid());
                        return mapper.toEntity(moduleService.createModule(moduleDTO));
                    })
                    .toList();

            // Update course with module IDs
            savedCourse.setModuleIds(modules.stream()
                    .map(Module::getId)
                    .collect(Collectors.toList()));

            finalCourse = courseRepo.save(savedCourse);
            courseDTO = mapper.toDTO(finalCourse);
            courseDTO.setModuleDTOS(modules.stream().map(mapper::toDTO).toList());
        }

        return courseDTO;
    }

    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        Course existingCourse = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Update basic course details
        existingCourse.setCourseCode(courseDTO.getCourseCode());
        existingCourse.setCourseName(courseDTO.getCourseName());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setDifficultyLevel(courseDTO.getDifficultyLevel());
        existingCourse.setPrerequisiteCourseIds(courseDTO.getPrerequisiteCourseIds());
        existingCourse.setIsActive(courseDTO.getIsActive());

        // Update Teacher Association
        if (courseDTO.getTeacherId() != null) {
            Teacher teacher = teacherRepo.findById(courseDTO.getTeacherId())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
            existingCourse.setTeacher(teacher);
        }

        List<ModuleDTO> newModules = new ArrayList<ModuleDTO>();
        // Update Modules
        if (courseDTO.getModuleDTOS() != null) {
            // First, remove existing modules
            if (existingCourse.getModuleIds() != null) {
                moduleRepo.deleteAllById(existingCourse.getModuleIds());
            }

            // Create new modules
            newModules = courseDTO.getModuleDTOS().stream()
                    .map(moduleDTO -> {
                        moduleDTO.setCourseId(courseId);
                        return moduleService.updateModule(moduleDTO.getId(),moduleDTO);
                    })
                    .toList();

            existingCourse.setModuleIds(newModules.stream()
                    .map(ModuleDTO::getId)
                    .collect(Collectors.toList()));
        }

        Course updatedCourse = courseRepo.save(existingCourse);
        courseDTO = mapper.toDTO(updatedCourse);
        courseDTO.setModuleDTOS(newModules);
        return courseDTO;
    }

    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return mapper.toDTO(course);
    }

    public void deleteCourse(Long courseId) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Remove teacher association
        if (course.getTeacher() != null) {
            course.getTeacher().getCourses().remove(course);
            course.setTeacher(null);
        }

        // Delete associated modules
        if (course.getModuleIds() != null) {
            moduleRepo.deleteAllById(course.getModuleIds());
        }

        courseRepo.delete(course);
    }

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepo.findAll();
        return courses.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}