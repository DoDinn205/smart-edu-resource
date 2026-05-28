package com.paq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ForumCategory;
import com.paq.pojo.ForumThread;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqForumThreadDTO;
import com.paq.pojo.response.ResForumThreadDTO;
import com.paq.repository.ForumCategoryRepository;
import com.paq.repository.ForumThreadRepository;
import com.paq.service.ForumThreadService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class ForumThreadServiceImpl implements ForumThreadService {

    @Autowired
    private ForumThreadRepository threadRepo;

    @Autowired
    private ForumCategoryRepository categoryRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResForumThreadDTO> getThreads(Map<String, String> params) {
        return this.threadRepo.getThreads(params).stream()
                .map(DTOMapper::toResForumThreadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResForumThreadDTO getThreadById(int id) {
        ForumThread thread = this.threadRepo.getThreadById(id);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        return DTOMapper.toResForumThreadDTO(thread);
    }

    @Override
    public ResForumThreadDTO createThread(ReqForumThreadDTO request) {
        User user = this.permissionService.getCurrentUser();
        ForumCategory category = this.categoryRepo.getCategoryById(request.getCategoryId());
        if (category == null) {
            throw new IdInvalidException("Forum category không tồn tại");
        }

        Date now = new Date();
        ForumThread thread = new ForumThread();
        thread.setTitle(request.getTitle());
        thread.setContent(request.getContent());
        thread.setCategoryId(category);
        thread.setCreatedBy(user);
        thread.setIsLock(Boolean.FALSE);
        thread.setIsDeleted(Boolean.FALSE);
        thread.setCreatedAt(now);
        thread.setUpdateAt(now);

        return DTOMapper.toResForumThreadDTO(this.threadRepo.addOrUpdateThread(thread));
    }

    @Override
    public ResForumThreadDTO updateThread(int id, ReqForumThreadDTO request) {
        this.permissionService.requireAdmin();

        ForumThread thread = this.threadRepo.getThreadById(id);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        ForumCategory category = this.categoryRepo.getCategoryById(request.getCategoryId());
        if (category == null) {
            throw new IdInvalidException("Forum category không tồn tại");
        }

        thread.setTitle(request.getTitle());
        thread.setContent(request.getContent());
        thread.setCategoryId(category);
        thread.setUpdateAt(new Date());

        return DTOMapper.toResForumThreadDTO(this.threadRepo.addOrUpdateThread(thread));
    }

    @Override
    public ResForumThreadDTO updateThreadLock(int id, Boolean isLock) {
        this.permissionService.requireAdmin();

        ForumThread thread = this.threadRepo.getThreadById(id);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        thread.setIsLock(Boolean.TRUE.equals(isLock));
        thread.setUpdateAt(new Date());

        return DTOMapper.toResForumThreadDTO(this.threadRepo.addOrUpdateThread(thread));
    }

    @Override
    public void deleteThread(int id) {
        this.permissionService.requireAdmin();

        ForumThread thread = this.threadRepo.getThreadById(id);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        this.threadRepo.deleteThread(id);
    }
}
