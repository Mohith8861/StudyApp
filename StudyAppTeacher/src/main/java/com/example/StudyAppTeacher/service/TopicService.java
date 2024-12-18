package com.example.StudyAppTeacher.service;

import com.example.StudyAppTeacher.dto.ResourceDTO;
import com.example.StudyAppTeacher.dto.TopicDTO;
import com.example.StudyAppTeacher.model.mongodb.Resource;
import com.example.StudyAppTeacher.model.mongodb.Topic;
import com.example.StudyAppTeacher.model.mongodb.Module;
import com.example.StudyAppTeacher.repo.mongodb.ModuleRepo;
import com.example.StudyAppTeacher.repo.mongodb.ResourceRepo;
import com.example.StudyAppTeacher.repo.mongodb.TopicRepo;
import com.example.StudyAppTeacher.utilities.Mapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepo topicRepo;
    private final ModuleRepo moduleRepo;
    private final ResourceService resourceService;
    private final Mapper mapper;
    private final ResourceRepo resourceRepo;

    @Autowired
    public TopicService(TopicRepo topicRepo, ModuleRepo moduleRepo,
                        ResourceService resourceService, Mapper mapper,ResourceRepo resourceRepo) {
        this.topicRepo = topicRepo;
        this.moduleRepo = moduleRepo;
        this.resourceService = resourceService;
        this.mapper = mapper;
        this.resourceRepo = resourceRepo;
    }

    public TopicDTO createTopic(TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setTopicName(topicDTO.getTopicName());
        topic.setTopicDescription(topicDTO.getTopicDescription());
        topic.setSequenceOrder(topicDTO.getSequenceOrder());
        topic.setModuleId(topicDTO.getModuleId());

        List<ResourceDTO> resources = new ArrayList<ResourceDTO>();

        // Handle Resources
        if (topicDTO.getResources() != null && !topicDTO.getResources().isEmpty()) {
            resources = topicDTO.getResources().stream()
                    .map(resourceService::createResource)
                    .toList();

        // topic.setResources(resources);
            topic.setResourceIds(resources.stream()
                    .map(ResourceDTO::getId)
                    .collect(Collectors.toList()));
        }

        Topic savedTopic = topicRepo.save(topic);

        // Update Module with Topic ID
        if (topic.getModuleId() != null) {
            Module module = moduleRepo.findById(topic.getModuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Module not found"));

            if (module.getTopics() == null) {
                module.setTopics(new ArrayList<>());
            }
            module.getTopics().add(savedTopic);
            moduleRepo.save(module);
        }
        topicDTO = mapper.toDTO(savedTopic);
        if(!resources.isEmpty()){
            topicDTO.setResources(resources);
//            topicDTO.setResourceIds();
        }
        return topicDTO;
    }

    public TopicDTO updateTopic(String topicId, TopicDTO topicDTO) {
        Topic existingTopic = topicRepo.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        existingTopic.setTopicName(topicDTO.getTopicName());
        existingTopic.setTopicDescription(topicDTO.getTopicDescription());
        existingTopic.setSequenceOrder(topicDTO.getSequenceOrder());
        existingTopic.setModuleId(topicDTO.getModuleId());

        // Update Resources
        if (topicDTO.getResources() != null) {
            // Remove existing resources
            if (existingTopic.getResourceIds() != null) {
                resourceRepo.deleteAllById(existingTopic.getResourceIds());
            }

            // Save new resources
            List<ResourceDTO> updatedResources = topicDTO.getResources().stream()
                    .map(resourceDTO -> {
                        if(resourceService.getResourceById(resourceDTO.getId()) != null){
                            resourceDTO.setTopicId(topicId);
                            return resourceService.updateResource(resourceDTO.getId() ,resourceDTO);
                        }
                        else {
                            return resourceDTO;
                        }
                    })
                    .toList();

            existingTopic.setResourceIds(updatedResources.stream().map(ResourceDTO::getId).collect(Collectors.toList()));

        }

        Topic updatedTopic = topicRepo.save(existingTopic);
        return mapper.toDTO(updatedTopic);
    }

    public TopicDTO getTopicById(String topicId) {
        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        return mapper.toDTO(topic);
    }

    public void deleteTopic(String topicId) {
        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));

        // Remove topic from module
        if (topic.getModuleId() != null) {
            Module module = moduleRepo.findById(topic.getModuleId())
                    .orElseThrow(() -> new EntityNotFoundException("Module not found"));

            if (module.getTopics() != null) {
                module.getTopics().remove(topic);
                moduleRepo.save(module);
            }
        }

        // Delete associated resources
        if (topic.getResourceIds() != null) {
            resourceRepo.deleteAllById(topic.getResourceIds().stream().toList());
        }

        topicRepo.delete(topic);
    }

    public List<TopicDTO> getTopicsByModuleId(String moduleId) {
        List<Topic> topics = topicRepo.findByModuleId(moduleId);
        return topics.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TopicDTO> getAllTopics(){
        List<Topic> topics = topicRepo.findAll();
        return topics.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());    }
}