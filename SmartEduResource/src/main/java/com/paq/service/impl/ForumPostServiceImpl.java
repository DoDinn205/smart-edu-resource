package com.paq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ForumPost;
import com.paq.pojo.ForumThread;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqForumPostDTO;
import com.paq.pojo.response.ResForumPostDTO;
import com.paq.repository.ForumPostRepository;
import com.paq.repository.ForumThreadRepository;
import com.paq.service.ForumPostService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;

@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    private ForumPostRepository postRepo;

    @Autowired
    private ForumThreadRepository threadRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResForumPostDTO> getPostsByThreadId(int threadId, Map<String, String> params) {
        ForumThread thread = this.threadRepo.getThreadById(threadId);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        return this.postRepo.getPostsByThreadId(threadId, params).stream()
                .map(DTOMapper::toResForumPostDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResForumPostDTO getPostById(int id) {
        ForumPost post = this.postRepo.getPostById(id);
        if (post == null) {
            throw new IdInvalidException("Forum post không tồn tại");
        }

        return DTOMapper.toResForumPostDTO(post);
    }

    @Override
    public ResForumPostDTO createPost(int threadId, ReqForumPostDTO request) {
        User user = this.permissionService.getCurrentUser();
        ForumThread thread = this.threadRepo.getThreadById(threadId);
        if (thread == null) {
            throw new IdInvalidException("Forum thread không tồn tại");
        }

        if (Boolean.TRUE.equals(thread.getIsLock())) {
            throw new PermissionException("Forum thread đã bị khóa");
        }

        Date now = new Date();
        ForumPost post = new ForumPost();
        post.setContent(request.getContent());
        post.setThreadId(thread);
        post.setUserId(user);
        post.setIsDeleted(Boolean.FALSE);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        thread.setUpdateAt(now);
        this.threadRepo.addOrUpdateThread(thread);

        return DTOMapper.toResForumPostDTO(this.postRepo.addOrUpdatePost(post));
    }

    @Override
    public ResForumPostDTO updatePost(int id, ReqForumPostDTO request) {
        this.permissionService.requireAdmin();

        ForumPost post = this.postRepo.getPostById(id);
        if (post == null) {
            throw new IdInvalidException("Forum post không tồn tại");
        }

        post.setContent(request.getContent());
        post.setUpdatedAt(new Date());

        return DTOMapper.toResForumPostDTO(this.postRepo.addOrUpdatePost(post));
    }

    @Override
    public void deletePost(int id) {
        this.permissionService.requireAdmin();

        ForumPost post = this.postRepo.getPostById(id);
        if (post == null) {
            throw new IdInvalidException("Forum post không tồn tại");
        }

        this.postRepo.deletePost(id);
    }
}
