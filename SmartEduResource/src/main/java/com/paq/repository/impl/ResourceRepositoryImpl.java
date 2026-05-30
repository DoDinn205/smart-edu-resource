package com.paq.repository.impl;

import com.paq.pojo.Resource;
import com.paq.pojo.ResourceRelation;
import com.paq.pojo.ResourceTag;
import com.paq.pojo.ResourceType;
import com.paq.pojo.Subject;
import com.paq.pojo.Topic;
import com.paq.repository.ResourceRepository;
import com.paq.utils.constant.FormatEnum;
import com.paq.utils.constant.LevelEnum;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
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
public class ResourceRepositoryImpl implements ResourceRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Resource> getResources(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Resource> q = b.createQuery(Resource.class);
        Root<Resource> root = q.from(Resource.class);
        root.fetch("uploadBy", JoinType.LEFT);
        root.fetch("subjectSet", JoinType.LEFT);
        root.fetch("topicSet", JoinType.LEFT);
        root.fetch("resourceTagSet", JoinType.LEFT);
        root.fetch("resourceTypeSet", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.or(
                b.isFalse(root.get("isDeleted")),
                b.isNull(root.get("isDeleted"))));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.or(
                        b.like(root.get("title"), String.format("%%%s%%", kw)),
                        b.like(root.get("description"), String.format("%%%s%%", kw))));
            }

            String level = params.get("level");
            if (level != null && !level.isEmpty()) {
                predicates.add(b.equal(root.get("level"), LevelEnum.valueOf(level)));
            }

            String format = params.get("format");
            if (format != null && !format.isEmpty()) {
                predicates.add(b.equal(root.get("format"), FormatEnum.valueOf(format)));
            }

            String uploadById = params.get("uploadById");
            if (uploadById != null && !uploadById.isEmpty()) {
                predicates.add(b.equal(root.get("uploadBy").get("id"), Integer.parseInt(uploadById)));
            }

            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                Join<Resource, Subject> subjectJoin = root.join("subjectSet", JoinType.INNER);
                predicates.add(b.equal(subjectJoin.get("id"), Integer.parseInt(subjectId)));
            }

            String topicId = params.get("topicId");
            if (topicId != null && !topicId.isEmpty()) {
                Join<Resource, Topic> topicJoin = root.join("topicSet", JoinType.INNER);
                predicates.add(b.equal(topicJoin.get("id"), Integer.parseInt(topicId)));
            }

            String tagId = params.get("tagId");
            if (tagId != null && !tagId.isEmpty()) {
                Join<Resource, ResourceTag> tagJoin = root.join("resourceTagSet", JoinType.INNER);
                predicates.add(b.equal(tagJoin.get("id"), Integer.parseInt(tagId)));
            }

            String typeId = params.get("typeId");
            if (typeId != null && !typeId.isEmpty()) {
                Join<Resource, ResourceType> typeJoin = root.join("resourceTypeSet", JoinType.INNER);
                predicates.add(b.equal(typeJoin.get("id"), Integer.parseInt(typeId)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Resource> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("resources.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Resource getResourceById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Resource> q = session.createQuery(
                    "SELECT DISTINCT r FROM Resource r "
                    + "LEFT JOIN FETCH r.uploadBy "
                    + "LEFT JOIN FETCH r.subjectSet "
                    + "LEFT JOIN FETCH r.topicSet "
                    + "LEFT JOIN FETCH r.resourceTagSet "
                    + "LEFT JOIN FETCH r.resourceTypeSet "
                    + "WHERE r.id = :id "
                    + "AND (r.isDeleted = false OR r.isDeleted IS NULL)",
                    Resource.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Resource getResourceByTitle(String title) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Resource> q = session.createNamedQuery("Resource.findByTitle", Resource.class);
            q.setParameter("title", title);
            Resource resource = q.getSingleResult();
            if (resource.getIsDeleted() != null && resource.getIsDeleted() == true) {
                return null;
            }

            return resource;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Resource addOrUpdateResource(Resource resource) {
        Session session = this.factory.getObject().getCurrentSession();
        if (resource.getId() != null) {
            return session.merge(resource);
        }

        session.persist(resource);
        return resource;
    }

    @Override
    public void deleteResource(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Resource resource = this.getResourceById(id);
        resource.setIsDeleted(Boolean.TRUE);
        session.merge(resource);
    }

    @Override
    public List<ResourceRelation> getRelationsBySourceId(int sourceId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<ResourceRelation> q = session.createQuery(
                "SELECT rr FROM ResourceRelation rr "
                + "JOIN FETCH rr.relatedId "
                + "WHERE rr.sourceId.id = :sourceId "
                + "AND (rr.relatedId.isDeleted = false OR rr.relatedId.isDeleted IS NULL)",
                ResourceRelation.class);
        q.setParameter("sourceId", sourceId);
        return q.getResultList();
    }

    @Override
    public void replaceRelations(Resource source, List<Resource> relatedResources) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<ResourceRelation> q = session.createQuery(
                "SELECT rr FROM ResourceRelation rr WHERE rr.sourceId.id = :sourceId",
                ResourceRelation.class);
        q.setParameter("sourceId", source.getId());
        q.getResultList().forEach(session::remove);

        for (Resource relatedResource : relatedResources) {
            ResourceRelation relation = new ResourceRelation();
            relation.setSourceId(source);
            relation.setRelatedId(relatedResource);
            relation.setRelatedType(1);
            session.persist(relation);
        }
    }

    @Override
    public List<Resource> getRelatedResources(int resourceId) {
        Session s = this.factory.getObject().getCurrentSession();

        Resource resource = s.get(Resource.class, resourceId);

        if (resource == null) {
            return new ArrayList<>();
        }

        Query<Resource> q = s.createQuery(
                "FROM Resource r WHERE r.id <> :id",
                Resource.class
        );

        q.setParameter("id", resourceId);

        return q.setMaxResults(5).getResultList();
    }
}
