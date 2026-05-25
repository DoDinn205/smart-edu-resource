package com.paq.repository.impl;

import com.paq.pojo.Enrollment;
import com.paq.repository.EnrollmentRepository;
import com.paq.utils.constant.EnrollmentStatusEnum;
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
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(int courseId, Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Enrollment> q = b.createQuery(Enrollment.class);
        Root<Enrollment> root = q.from(Enrollment.class);
        root.fetch("courseId", JoinType.LEFT);
        root.fetch("studentId", JoinType.LEFT).fetch("userId", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(root.get("courseId").get("id"), courseId));

        if (params != null) {
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), EnrollmentStatusEnum.valueOf(status)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Enrollment> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("enrollments.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Enrollment> q = session.createQuery(
                    "SELECT e FROM Enrollment e "
                    + "JOIN FETCH e.courseId "
                    + "JOIN FETCH e.studentId s "
                    + "JOIN FETCH s.userId "
                    + "WHERE e.id = :id",
                    Enrollment.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Enrollment addOrUpdateEnrollment(Enrollment enrollment) {
        Session session = this.factory.getObject().getCurrentSession();
        if (enrollment.getId() != null) {
            return session.merge(enrollment);
        }

        session.persist(enrollment);
        return enrollment;
    }

    @Override
    public boolean existsByCourseId(int courseId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(e.id) FROM Enrollment e WHERE e.courseId.id = :courseId",
                Long.class);
        q.setParameter("courseId", courseId);
        return q.getSingleResult() > 0;
    }
}
