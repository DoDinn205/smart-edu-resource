package com.paq.utils;

import com.paq.pojo.ResourceTag;
import com.paq.pojo.ResourceType;
import com.paq.pojo.Subject;
import com.paq.pojo.Topic;
import com.paq.pojo.User;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResSubjectDTO;
import com.paq.pojo.response.ResUserDTO;

public class DTOMapper {

    public static ResUserDTO toResUserDTO(User user) {
        if (user == null) {
            return null;
        }

        ResUserDTO dto = new ResUserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);

        return dto;
    }

    public static ResSubjectDTO toResSubjectDTO(Subject subject) {
        if (subject == null) {
            return null;
        }

        ResSubjectDTO dto = new ResSubjectDTO();
        dto.setId(subject.getId());
        dto.setCode(subject.getCode());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        dto.setCreatedAt(subject.getCreatedAt());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(Topic topic) {
        if (topic == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(topic.getId());
        dto.setName(topic.getName());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(ResourceType resourceType) {
        if (resourceType == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(resourceType.getId());
        dto.setName(resourceType.getName());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(ResourceTag resourceTag) {
        if (resourceTag == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(resourceTag.getId());
        dto.setName(resourceTag.getName());

        return dto;
    }
}
