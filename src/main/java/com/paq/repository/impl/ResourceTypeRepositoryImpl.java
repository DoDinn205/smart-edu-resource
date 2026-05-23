package com.paq.repository.impl;

import com.paq.pojo.ResourceType;
import com.paq.repository.ResourceTypeRepository;
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
public class ResourceTypeRepositoryImpl implements ResourceTypeRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ResourceType> getResourceTypes(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ResourceType> q = b.createQuery(ResourceType.class);
        Root<ResourceType> root = q.from(ResourceType.class);
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

        Query<ResourceType> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("resource_types.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public ResourceType getResourceTypeById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(ResourceType.class, id);
    }

    @Override
    public ResourceType getResourceTypeByName(String name) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ResourceType> q = session.createNamedQuery("ResourceType.findByName", ResourceType.class);
            q.setParameter("name", name);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ResourceType addOrUpdateResourceType(ResourceType resourceType) {
        Session session = this.factory.getObject().getCurrentSession();
        if (resourceType.getId() != null) {
            return session.merge(resourceType);
        }
        session.persist(resourceType);
        return resourceType;
    }

    @Override
    public void deleteResourceType(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ResourceType resourceType = this.getResourceTypeById(id);
        session.remove(resourceType);
    }
}
