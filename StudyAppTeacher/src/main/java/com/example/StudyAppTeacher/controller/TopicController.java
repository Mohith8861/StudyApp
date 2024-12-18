package com.example.StudyAppTeacher.controller;

import com.example.StudyAppTeacher.dto.TopicDTO;
import com.example.StudyAppTeacher.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {
        return new ResponseEntity<>(topicService.createTopic(topicDTO), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable String id) {
        return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<TopicDTO>> getTopicsByModuleId(@PathVariable String moduleId) {
        try{
            return ResponseEntity.ok(topicService.getTopicsByModuleId(moduleId));
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDTO> updateTopic(
            @PathVariable String id,
            @RequestBody TopicDTO topicDTO
    ) {
        return ResponseEntity.ok(topicService.updateTopic(id, topicDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}