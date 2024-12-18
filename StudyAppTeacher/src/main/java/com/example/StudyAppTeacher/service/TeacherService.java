package com.example.StudyAppTeacher.service;

import com.example.StudyAppTeacher.dto.TeacherDTO;
import com.example.StudyAppTeacher.model.jpa.Course;
import com.example.StudyAppTeacher.model.jpa.Teacher;
import com.example.StudyAppTeacher.repo.jpa.CourseRepo;
import com.example.StudyAppTeacher.repo.jpa.TeacherRepo;
import com.example.StudyAppTeacher.utilities.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;
    private final Mapper mapper;

    @Autowired
    public TeacherService(TeacherRepo teacherRepo, CourseRepo courseRepo, Mapper mapper) {
        this.teacherRepo = teacherRepo;
        this.courseRepo = courseRepo;
        this.mapper = mapper;
    }


    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = mapper.toEntity(teacherDTO);

        // Handle course associations if course IDs are provided
        if (teacherDTO.getCourseIds() != null && !teacherDTO.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepo.findAllById(teacherDTO.getCourseIds());
            teacher.setCourses(courses);
        }

        Teacher savedTeacher = teacherRepo.save(teacher);
        return mapper.toDTO(savedTeacher);
    }

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepo.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public TeacherDTO getTeacherById(Long teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));
        return mapper.toDTO(teacher);
    }

    public TeacherDTO updateTeacher(Long teacherId, TeacherDTO teacherDTO) {
        Teacher existingTeacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));

        // Update basic teacher information
        existingTeacher.setFirstName(teacherDTO.getFirstName());
        existingTeacher.setLastName(teacherDTO.getLastName());
        existingTeacher.setEmail(teacherDTO.getEmail());
        existingTeacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        existingTeacher.setDateOfBirth(teacherDTO.getDateOfBirth());
        existingTeacher.setGender(teacherDTO.getGender());
        existingTeacher.setSpecialization(teacherDTO.getSpecialization());
        existingTeacher.setIsActive(teacherDTO.getIsActive());

        // Handle course associations
        if (teacherDTO.getCourseIds() != null) {
            List<Course> courses = courseRepo.findAllById(teacherDTO.getCourseIds());
            existingTeacher.setCourses(courses);
        }

        Teacher updatedTeacher = teacherRepo.save(existingTeacher);
        return mapper.toDTO(updatedTeacher);
    }


    public void deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));

        // Disassociate courses before deleting
        if (teacher.getCourses() != null) {
            teacher.getCourses().forEach(course -> course.setTeacher(null));
            courseRepo.saveAll(teacher.getCourses());
        }

        teacherRepo.delete(teacher);
    }
}

