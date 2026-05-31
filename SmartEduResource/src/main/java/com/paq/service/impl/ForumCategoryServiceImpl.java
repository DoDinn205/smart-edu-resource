package com.paq.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ForumCategory;
import com.paq.pojo.request.ReqForumCategoryDTO;
import com.paq.pojo.response.ResForumCategoryDTO;
import com.paq.repository.ForumCategoryRepository;
import com.paq.service.ForumCategoryService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {

    @Autowired
    private ForumCategoryRepository categoryRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResForumCategoryDTO> getCategories(Map<String, String> params) {
        return this.categoryRepo.getCategories(params).stream()
                .map(DTOMapper::toResForumCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countCategories(Map<String, String> params) {
        return this.categoryRepo.countCategories(params);
    }

    @Override
    public ResForumCategoryDTO getCategoryById(int id) {
        ForumCategory category = this.categoryRepo.getCategoryById(id);
        if (category == null) {
            throw new IdInvalidException("Forum category không tồn tại");
        }

        return DTOMapper.toResForumCategoryDTO(category);
    }

    @Override
    public ResForumCategoryDTO createCategory(ReqForumCategoryDTO request) {
        this.permissionService.requireAdmin();

        ForumCategory category = new ForumCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIsDeleted(Boolean.FALSE);

        return DTOMapper.toResForumCategoryDTO(this.categoryRepo.addOrUpdateCategory(category));
    }

    @Override
    public ResForumCategoryDTO updateCategory(int id, ReqForumCategoryDTO request) {
        this.permissionService.requireAdmin();

        ForumCategory category = this.categoryRepo.getCategoryById(id);
        if (category == null) {
            throw new IdInvalidException("Forum category không tồn tại");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return DTOMapper.toResForumCategoryDTO(this.categoryRepo.addOrUpdateCategory(category));
    }

    @Override
    public void deleteCategory(int id) {
        this.permissionService.requireAdmin();

        ForumCategory category = this.categoryRepo.getCategoryById(id);
        if (category == null) {
            throw new IdInvalidException("Forum category không tồn tại");
        }

        this.categoryRepo.deleteCategory(id);
    }
}
