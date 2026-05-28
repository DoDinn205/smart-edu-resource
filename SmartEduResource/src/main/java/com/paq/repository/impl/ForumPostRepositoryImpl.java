package com.paq.repository.impl;

import com.paq.pojo.ForumPost;
import com.paq.repository.ForumPostRepository;
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
public class ForumPostRepositoryImpl implements ForumPostRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ForumPost> getPostsByThreadId(int threadId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ForumPost> q = b.createQuery(ForumPost.class);
        Root<ForumPost> root = q.from(ForumPost.class);
        root.fetch("threadId", JoinType.INNER);
        root.fetch("userId", JoinType.INNER);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("threadId").get("id"), threadId));
        predicates.add(b.or(b.isFalse(root.get("isDeleted")), b.isNull(root.get("isDeleted"))));
        predicates.add(b.or(
                b.isFalse(root.get("threadId").get("isDeleted")),
                b.isNull(root.get("threadId").get("isDeleted"))));

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.asc(root.get("createdAt")), b.asc(root.get("id")));

        Query<ForumPost> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("forum_posts.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            query.setMaxResults(pageSize);
            query.setFirstResult((page - 1) * pageSize);
        }

        return query.getResultList();
    }

    @Override
    public ForumPost getPostById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ForumPost> q = session.createQuery(
                    "SELECT p FROM ForumPost p "
                    + "JOIN FETCH p.threadId t "
                    + "JOIN FETCH p.userId u "
                    + "WHERE p.id = :id "
                    + "AND (p.isDeleted = false OR p.isDeleted IS NULL) "
                    + "AND (t.isDeleted = false OR t.isDeleted IS NULL)",
                    ForumPost.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ForumPost addOrUpdatePost(ForumPost post) {
        Session session = this.factory.getObject().getCurrentSession();
        if (post.getId() != null) {
            return session.merge(post);
        }

        session.persist(post);
        return post;
    }

    @Override
    public void deletePost(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ForumPost post = this.getPostById(id);
        post.setIsDeleted(Boolean.TRUE);
        session.merge(post);
    }
}
