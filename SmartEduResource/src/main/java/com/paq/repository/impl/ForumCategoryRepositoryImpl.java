package com.paq.repository.impl;

import com.paq.pojo.ForumCategory;
import com.paq.repository.ForumCategoryRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class ForumCategoryRepositoryImpl implements ForumCategoryRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumCategory> getCategories(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ForumCategory> q = b.createQuery(ForumCategory.class);
        Root<ForumCategory> root = q.from(ForumCategory.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.or(b.isFalse(root.get("isDeleted")), b.isNull(root.get("isDeleted"))));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<ForumCategory> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("forum_categories.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            query.setMaxResults(pageSize);
            query.setFirstResult((page - 1) * pageSize);
        }

        return query.getResultList();
    }

    @Override
    public ForumCategory getCategoryById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumCategory category = session.get(ForumCategory.class, id);
        if (category == null || Boolean.TRUE.equals(category.getIsDeleted())) {
            return null;
        }

        return category;
    }

    @Override
    public ForumCategory getCategoryByName(String name) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ForumCategory> q = session.createNamedQuery("ForumCategory.findByName", ForumCategory.class);
            q.setParameter("name", name);
            ForumCategory category = q.getSingleResult();
            if (Boolean.TRUE.equals(category.getIsDeleted())) {
                return null;
            }

            return category;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ForumCategory addOrUpdateCategory(ForumCategory category) {
        Session session = this.factory.getObject().getCurrentSession();
        if (category.getId() != null) {
            return session.merge(category);
        }

        session.persist(category);
        return category;
    }

    @Override
    public void deleteCategory(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumCategory category = this.getCategoryById(id);
        category.setIsDeleted(Boolean.TRUE);
        session.merge(category);
    }
}
