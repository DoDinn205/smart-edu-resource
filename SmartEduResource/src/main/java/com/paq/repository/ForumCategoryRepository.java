package com.paq.repository;

import com.paq.pojo.ForumCategory;
import java.util.List;
import java.util.Map;

public interface ForumCategoryRepository {

    List<ForumCategory> getCategories(Map<String, String> params);

    long countCategories(Map<String, String> params);

    ForumCategory getCategoryById(int id);

    ForumCategory getCategoryByName(String name);

    ForumCategory addOrUpdateCategory(ForumCategory category);

    void deleteCategory(int id);
}
