package com.paq.service;

import com.paq.pojo.request.ReqForumThreadDTO;
import com.paq.pojo.response.ResForumThreadDTO;
import java.util.List;
import java.util.Map;

public interface ForumThreadService {

    List<ResForumThreadDTO> getThreads(Map<String, String> params);

    ResForumThreadDTO getThreadById(int id);

    ResForumThreadDTO createThread(ReqForumThreadDTO request);

    ResForumThreadDTO updateThread(int id, ReqForumThreadDTO request);

    ResForumThreadDTO updateThreadLock(int id, Boolean isLock);

    void deleteThread(int id);
}
