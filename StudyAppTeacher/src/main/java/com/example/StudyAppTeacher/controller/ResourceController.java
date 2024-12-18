package com.example.StudyAppTeacher.controller;

import com.example.StudyAppTeacher.dto.ResourceDTO;
import com.example.StudyAppTeacher.enums.ResourceType;
import com.example.StudyAppTeacher.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping
    public ResponseEntity<ResourceDTO> createResource(@RequestBody ResourceDTO resourceDTO) {
        return new ResponseEntity<>(resourceService.createResource(resourceDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable String id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ResourceDTO>> getResourcesByTopicId(@PathVariable String topicId) {
        return ResponseEntity.ok(resourceService.getResourcesByTopicId(topicId));
    }

    @GetMapping("/type/{resourceType}")
    public ResponseEntity<List<ResourceDTO>> getResourcesByType(@PathVariable ResourceType resourceType) {
        return ResponseEntity.ok(resourceService.getResourcesByResourceType(resourceType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDTO> updateResource(
            @PathVariable String id,
            @RequestBody ResourceDTO resourceDTO
    ) {
        return ResponseEntity.ok(resourceService.updateResource(id, resourceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}