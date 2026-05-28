package com.paq.repository.impl;

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

import com.paq.pojo.Course;
import com.paq.pojo.Subject;
import com.paq.repository.CourseRepository;
import com.paq.utils.constant.LevelEnum;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Course> q = b.createQuery(Course.class);
        Root<Course> root = q.from(Course.class);
        root.fetch("subjectSet", JoinType.LEFT);
        root.fetch("createdBy", JoinType.LEFT);
        root.fetch("lecturerId", JoinType.LEFT).fetch("userId", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.or(
                b.isFalse(root.get("isDeleted")),
                b.isNull(root.get("isDeleted"))));

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.or(
                        b.like(root.get("name"), String.format("%%%s%%", kw)),
                        b.like(root.get("description"), String.format("%%%s%%", kw))));
            }

            String isPaid = params.get("isPaid");
            if (isPaid != null && !isPaid.isEmpty()) {
                predicates.add(b.equal(root.get("isPaid"), Boolean.parseBoolean(isPaid)));
            }

            String targetLevel = params.get("targetLevel");
            if (targetLevel != null && !targetLevel.isEmpty()) {
                predicates.add(b.equal(root.get("targetLevel"), LevelEnum.valueOf(targetLevel)));
            }

            String subjectId = params.get("subjectId");
            if (subjectId != null && !subjectId.isEmpty()) {
                Join<Course, Subject> subjectJoin = root.join("subjectSet", JoinType.INNER);
                predicates.add(b.equal(subjectJoin.get("id"), Integer.parseInt(subjectId)));
            }

            String lecturerId = params.get("lecturerId");
            if (lecturerId != null && !lecturerId.isEmpty()) {
                predicates.add(b.equal(root.get("lecturerId").get("id"), Integer.parseInt(lecturerId)));
            }

            String createdBy = params.get("createdBy");
            if (createdBy != null && !createdBy.isEmpty()) {
                predicates.add(b.equal(root.get("createdBy").get("id"), Integer.parseInt(createdBy)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));

        q.orderBy(b.desc(root.get("id")));

        Query<Course> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("courses.page_size", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Course getCourseById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Course> q = session.createQuery(
                    "SELECT DISTINCT c FROM Course c "
                    + "LEFT JOIN FETCH c.subjectSet "
                    + "LEFT JOIN FETCH c.enrollmentSet "
                    + "LEFT JOIN FETCH c.createdBy "
                    + "LEFT JOIN FETCH c.lecturerId l "
                    + "LEFT JOIN FETCH l.userId "
                    + "WHERE c.id = :id "
                    + "AND (c.isDeleted = false OR c.isDeleted IS NULL)",
                    Course.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Course getCourseByName(String name) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Course> q = session.createNamedQuery("Course.findByName", Course.class);
            q.setParameter("name", name);
            Course course = q.getSingleResult();
            if (course.getIsDeleted() != null && course.getIsDeleted() == true) {
                return null;
            }

            return course;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public long countEnrollmentsByCourseId(int courseId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(e.id) FROM Enrollment e WHERE e.courseId.id = :courseId",
                Long.class);
        q.setParameter("courseId", courseId);
        Long result = q.getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public Course addOrUpdateCourse(Course course) {
        Session session = this.factory.getObject().getCurrentSession();
        if (course.getId() != null) {
            return session.merge(course);
        }

        session.persist(course);
        return course;
    }

    @Override
    public void deleteCourse(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Course course = session.get(Course.class, id);
        course.setIsDeleted(Boolean.TRUE);
        session.merge(course);
    }
}
