package com.paq.repository.impl;

import com.paq.pojo.Subject;
import com.paq.repository.SubjectRepository;
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
public class SubjectRepositoryImpl implements SubjectRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Subject> getSubjects(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Subject> q = b.createQuery(Subject.class);
        Root<Subject> root = q.from(Subject.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.or(
                        b.like(root.get("name"), String.format("%%%s%%", kw)),
                        b.like(root.get("code"), String.format("%%%s%%", kw))));
            }

            q.where(predicates.toArray(Predicate[]::new));
        }

        q.orderBy(b.desc(root.get("id")));

        Query<Subject> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("subjects.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Subject getSubjectById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Subject.class, id);
    }

    @Override
    public Subject getSubjectByName(String name) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Subject> q = session.createNamedQuery("Subject.findByName", Subject.class);
            q.setParameter("name", name);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Subject getSubjectByCode(String code) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Subject> q = session.createNamedQuery("Subject.findByCode", Subject.class);
            q.setParameter("code", code);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Subject addOrUpdateSubject(Subject subject) {
        Session session = this.factory.getObject().getCurrentSession();
        if (subject.getId() != null) {
            return session.merge(subject);
        }
        session.persist(subject);
        return subject;
    }

    @Override
    public void deleteSubject(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Subject subject = this.getSubjectById(id);
        session.remove(subject);
    }
}
