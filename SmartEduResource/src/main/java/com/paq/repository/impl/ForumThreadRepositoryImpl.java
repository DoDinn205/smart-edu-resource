package com.paq.repository.impl;

import com.paq.pojo.ForumThread;
import com.paq.repository.ForumThreadRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
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
public class ForumThreadRepositoryImpl implements ForumThreadRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumThread> getThreads(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ForumThread> q = b.createQuery(ForumThread.class);
        Root<ForumThread> root = q.from(ForumThread.class);
        root.fetch("categoryId", JoinType.INNER);
        root.fetch("createdBy", JoinType.INNER);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.or(b.isFalse(root.get("isDeleted")), b.isNull(root.get("isDeleted"))));
        predicates.add(b.or(
                b.isFalse(root.get("categoryId").get("isDeleted")),
                b.isNull(root.get("categoryId").get("isDeleted"))));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String categoryId = params.get("categoryId");
            if (categoryId != null && !categoryId.isEmpty()) {
                predicates.add(b.equal(root.get("categoryId").get("id"), Integer.parseInt(categoryId)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("updateAt")), b.desc(root.get("createdAt")), b.desc(root.get("id")));

        Query<ForumThread> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("forum_threads.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            query.setMaxResults(pageSize);
            query.setFirstResult((page - 1) * pageSize);
        }

        return query.getResultList();
    }

    @Override
    public ForumThread getThreadById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ForumThread> q = session.createQuery(
                    "SELECT DISTINCT t FROM ForumThread t "
                    + "JOIN FETCH t.categoryId c "
                    + "JOIN FETCH t.createdBy u "
                    + "WHERE t.id = :id "
                    + "AND (t.isDeleted = false OR t.isDeleted IS NULL) "
                    + "AND (c.isDeleted = false OR c.isDeleted IS NULL)",
                    ForumThread.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ForumThread addOrUpdateThread(ForumThread thread) {
        Session session = this.factory.getObject().getCurrentSession();
        if (thread.getId() != null) {
            return session.merge(thread);
        }

        session.persist(thread);
        return thread;
    }

    @Override
    public void deleteThread(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumThread thread = this.getThreadById(id);
        thread.setIsDeleted(Boolean.TRUE);
        session.merge(thread);
    }
}
