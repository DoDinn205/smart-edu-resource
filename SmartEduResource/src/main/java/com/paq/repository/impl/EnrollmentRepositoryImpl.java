/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Enrollment;
import com.paq.repository.EnrollmentRepository;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Repository
@Transactional
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public boolean existsByStudentAndCourse(int studentId, int courseId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Long> q = s.createQuery(
                "SELECT COUNT(e) FROM Enrollment e "
                + "WHERE e.studentId.id=:studentId AND e.courseId.id=:courseId",
                Long.class
        );
        q.setParameter("studentId", studentId);
        q.setParameter("courseId", courseId);
        return q.getSingleResult() > 0;
    }

    @Override
    public boolean existsByCourseIdAndUserId(int courseId, int userId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Long> q = s.createQuery(
                "SELECT COUNT(e) FROM Enrollment e "
                + "WHERE e.courseId.id=:courseId AND e.studentId.userId.id=:userId",
                Long.class
        );
        q.setParameter("courseId", courseId);
        q.setParameter("userId", userId);
        return q.getSingleResult() > 0;
    }

    @Override
    public Enrollment addEnrollment(Enrollment e) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(e);
        return e;
    }

    @Override
    public Enrollment addOrUpdateEnrollment(Enrollment enrollment) {
        Session s = this.factory.getObject().getCurrentSession();
        if (enrollment.getId() != null) {
            return s.merge(enrollment);
        }
        s.persist(enrollment);
        return enrollment;
    }

    @Override
    public Enrollment getEnrollmentById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Enrollment.class, id);
    }

    @Override
    public Enrollment findByCourseAndStudent(int courseId, int studentId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Enrollment> q = s.createQuery(
                "FROM Enrollment e WHERE e.courseId.id = :courseId AND e.studentId.id = :studentId",
                Enrollment.class
        );
        q.setParameter("courseId", courseId);
        q.setParameter("studentId", studentId);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query<Enrollment> q = s.createQuery(
                "FROM Enrollment e WHERE e.studentId.id=:studentId", Enrollment.class
        );
        q.setParameter("studentId", studentId);
        return q.getResultList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Enrollment> q = s.createQuery(
                "FROM Enrollment e WHERE e.studentId.userId.username=:username",
                Enrollment.class
        );

        q.setParameter("username", username);
        return q.getResultList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(int courseId, Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "FROM Enrollment e WHERE e.courseId.id = :courseId";

        if (params != null && params.containsKey("status")) {
            hql += " AND e.status = :status";
        }

        Query<Enrollment> q = s.createQuery(hql, Enrollment.class);
        q.setParameter("courseId", courseId);

        if (params != null && params.containsKey("status")) {
            q.setParameter("status", params.get("status"));
        }

        return q.getResultList();
    }

    @Override
    public List<Enrollment> getMyEnrollments(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Enrollment> q = s.createQuery(
                "FROM Enrollment e WHERE e.studentId.id = :studentId ORDER BY e.enrollDate DESC",
                Enrollment.class
        );
        q.setParameter("studentId", studentId);
        return q.getResultList();
    }
}
