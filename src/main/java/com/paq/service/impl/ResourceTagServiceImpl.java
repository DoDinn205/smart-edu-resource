package com.paq.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ResourceTag;
import com.paq.pojo.request.ReqCategoryDTO;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.repository.ResourceTagRepository;
import com.paq.service.ResourceTagService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class ResourceTagServiceImpl implements ResourceTagService {

    @Autowired
    private ResourceTagRepository resourceTagRepo;

    @Override
    public List<ResCategoryDTO> getResourceTags(Map<String, String> params) {
        return this.resourceTagRepo.getResourceTags(params).stream()
                .map(DTOMapper::toResCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResCategoryDTO getResourceTagById(int id) {
        ResourceTag resourceTag = this.resourceTagRepo.getResourceTagById(id);
        if (resourceTag == null) {
            throw new IdInvalidException("Resource tag không tồn tại");
        }

        return DTOMapper.toResCategoryDTO(resourceTag);
    }

    @Override
    public ResCategoryDTO createResourceTag(ReqCategoryDTO request) {
        ResourceTag resourceTag = new ResourceTag();
        resourceTag.setName(request.getName());

        return DTOMapper.toResCategoryDTO(this.resourceTagRepo.addOrUpdateResourceTag(resourceTag));
    }

    @Override
    public ResCategoryDTO updateResourceTag(int id, ReqCategoryDTO request) {
        ResourceTag resourceTag = this.resourceTagRepo.getResourceTagById(id);
        if (resourceTag == null) {
            throw new IdInvalidException("Resource tag không tồn tại");
        }

        resourceTag.setName(request.getName());

        return DTOMapper.toResCategoryDTO(this.resourceTagRepo.addOrUpdateResourceTag(resourceTag));
    }

    @Override
    public void deleteResourceTag(int id) {
        if (this.resourceTagRepo.getResourceTagById(id) == null) {
            throw new IdInvalidException("Resource tag không tồn tại");
        }

        this.resourceTagRepo.deleteResourceTag(id);
    }
}
