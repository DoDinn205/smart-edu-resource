package com.paq.repository;

import com.paq.pojo.ForumThread;
import java.util.List;
import java.util.Map;

public interface ForumThreadRepository {

    List<ForumThread> getThreads(Map<String, String> params);

    ForumThread getThreadById(int id);

    ForumThread addOrUpdateThread(ForumThread thread);

    void deleteThread(int id);
}
