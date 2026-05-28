package com.paq.repository;

import com.paq.pojo.ForumPost;
import java.util.List;
import java.util.Map;

public interface ForumPostRepository {

    List<ForumPost> getPostsByThreadId(int threadId, Map<String, String> params);

    ForumPost getPostById(int id);

    ForumPost addOrUpdatePost(ForumPost post);

    void deletePost(int id);
}
