package com.example.StudyAppTeacher.service;

import com.example.StudyAppTeacher.dto.ResourceDTO;
import com.example.StudyAppTeacher.enums.ResourceType;
import com.example.StudyAppTeacher.model.mongodb.Resource;
import com.example.StudyAppTeacher.model.mongodb.Topic;
import com.example.StudyAppTeacher.repo.mongodb.ResourceRepo;
import com.example.StudyAppTeacher.repo.mongodb.TopicRepo;
import com.example.StudyAppTeacher.utilities.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepo resourceRepo;
    private final TopicRepo topicRepo;
    private final Mapper mapper;

    @Autowired
    public ResourceService(ResourceRepo resourceRepo, TopicRepo topicRepo, Mapper mapper) {
        this.resourceRepo = resourceRepo;
        this.topicRepo = topicRepo;
        this.mapper = mapper;
    }

    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        Resource resource = mapper.toEntity(resourceDTO);
        resource.setUploadDate(LocalDateTime.now());

        Resource savedResource = new Resource();

        // Update Topic with Resource ID
        if (resource.getTopicId() != null) {
            Topic topic = topicRepo.findById(resource.getTopicId())
                    .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

            if (topic.getResourceIds() == null) {
                topic.setResourceIds(new ArrayList<>());
            }
//            resource.setTopicName(topic.getTopicName());
            savedResource = resourceRepo.save(resource);
            topic.getResourceIds().add(savedResource.getId());

            topicRepo.save(topic);
        }
        return mapper.toDTO(savedResource);
    }

    public ResourceDTO updateResource(String resourceId, ResourceDTO resourceDTO) {
        Resource existingResource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        existingResource.setName((resourceDTO.getName() != null) ? resourceDTO.getName(): existingResource.getName());
        existingResource.setDescription((resourceDTO.getDescription() != null) ? resourceDTO.getDescription():existingResource.getDescription());
        existingResource.setResourceType((resourceDTO.getResourceType() != null) ? resourceDTO.getResourceType():existingResource.getResourceType());
        existingResource.setResourceUrl((resourceDTO.getResourceUrl() != null) ? resourceDTO.getResourceUrl():existingResource.getResourceUrl());
        existingResource.setFileSize((resourceDTO.getFileSize() != null) ? resourceDTO.getFileSize():existingResource.getFileSize());
        existingResource.setTopicId((resourceDTO.getTopicId()!=null) ? resourceDTO.getTopicId():existingResource.getTopicId());
//        existingResource.setTopicName((resourceDTO.getTopicName()!=null) ? resourceDTO.getTopicName():existingResource.getTopicName());

        Resource updatedResource = resourceRepo.save(existingResource);
        return mapper.toDTO(updatedResource);
    }

    public ResourceDTO getResourceById(String resourceId) {
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));
        return mapper.toDTO(resource);
    }

    public void deleteResource(String resourceId) {
        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        // Remove resource from topic
        if (resource.getTopicId() != null) {
            Topic topic = topicRepo.findById(resource.getTopicId())
                    .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

            if (topic.getResourceIds()!= null) {
                topic.getResourceIds().remove(resource.getId());
                topicRepo.save(topic);
            }
        }

        resourceRepo.delete(resource);
    }

    public List<ResourceDTO> getResourcesByTopicId(String topicId) {
        List<Resource> resources = resourceRepo.findByTopicId(topicId);
        return resources.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> getResourcesByResourceType(ResourceType type){
        List<Resource> resources = resourceRepo.findByResourceType(type);
        return resources.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public List<ResourceDTO> getAllResources(){
        List<Resource> resources = resourceRepo.findAll();
        return resources.stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}