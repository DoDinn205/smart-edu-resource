package com.paq.service.impl;

import com.paq.pojo.Topic;
import com.paq.pojo.request.ReqCategoryDTO;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.repository.TopicRepository;
import com.paq.service.TopicService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepo;

    @Override
    public List<ResCategoryDTO> getTopics(Map<String, String> params) {
        return this.topicRepo.getTopics(params).stream()
                .map(DTOMapper::toResCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResCategoryDTO getTopicById(int id) {
        Topic topic = this.topicRepo.getTopicById(id);
        if (topic == null) {
            throw new IdInvalidException("Topic khong ton tai");
        }

        return DTOMapper.toResCategoryDTO(topic);
    }

    @Override
    public ResCategoryDTO createTopic(ReqCategoryDTO request) {
        Topic topic = new Topic();
        topic.setName(request.getName());

        return DTOMapper.toResCategoryDTO(this.topicRepo.addOrUpdateTopic(topic));
    }

    @Override
    public ResCategoryDTO updateTopic(int id, ReqCategoryDTO request) {
        Topic topic = this.topicRepo.getTopicById(id);
        if (topic == null) {
            throw new IdInvalidException("Topic khong ton tai");
        }

        topic.setName(request.getName());

        return DTOMapper.toResCategoryDTO(this.topicRepo.addOrUpdateTopic(topic));
    }

    @Override
    public void deleteTopic(int id) {
        if (this.topicRepo.getTopicById(id) == null) {
            throw new IdInvalidException("Topic khong ton tai");
        }

        this.topicRepo.deleteTopic(id);
    }
}
