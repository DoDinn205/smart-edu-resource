package com.paq.repository.impl;

import com.paq.pojo.ResourceTag;
import com.paq.repository.ResourceTagRepository;
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
public class ResourceTagRepositoryImpl implements ResourceTagRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ResourceTag> getResourceTags(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ResourceTag> q = b.createQuery(ResourceTag.class);
        Root<ResourceTag> root = q.from(ResourceTag.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        q.orderBy(b.desc(root.get("id")));

        Query<ResourceTag> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("resource_tags.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public ResourceTag getResourceTagById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(ResourceTag.class, id);
    }

    @Override
    public ResourceTag getResourceTagByName(String name) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ResourceTag> q = session.createNamedQuery("ResourceTag.findByName", ResourceTag.class);
            q.setParameter("name", name);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ResourceTag addOrUpdateResourceTag(ResourceTag resourceTag) {
        Session session = this.factory.getObject().getCurrentSession();
        if (resourceTag.getId() != null) {
            return session.merge(resourceTag);
        }
        session.persist(resourceTag);
        return resourceTag;
    }

    @Override
    public void deleteResourceTag(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ResourceTag resourceTag = this.getResourceTagById(id);
        session.remove(resourceTag);
    }
}
