package com.paq.service;

import com.paq.pojo.request.ReqForumPostDTO;
import com.paq.pojo.response.ResForumPostDTO;
import java.util.List;
import java.util.Map;

public interface ForumPostService {

    List<ResForumPostDTO> getPostsByThreadId(int threadId, Map<String, String> params);

    ResForumPostDTO getPostById(int id);

    ResForumPostDTO createPost(int threadId, ReqForumPostDTO request);

    ResForumPostDTO updatePost(int id, ReqForumPostDTO request);

    void deletePost(int id);
}
