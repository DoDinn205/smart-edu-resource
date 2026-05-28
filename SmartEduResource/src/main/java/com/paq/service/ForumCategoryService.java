package com.paq.service;

import com.paq.pojo.request.ReqForumCategoryDTO;
import com.paq.pojo.response.ResForumCategoryDTO;
import java.util.List;
import java.util.Map;

public interface ForumCategoryService {

    List<ResForumCategoryDTO> getCategories(Map<String, String> params);

    ResForumCategoryDTO getCategoryById(int id);

    ResForumCategoryDTO createCategory(ReqForumCategoryDTO request);

    ResForumCategoryDTO updateCategory(int id, ReqForumCategoryDTO request);

    void deleteCategory(int id);
}
