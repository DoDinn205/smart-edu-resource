/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Course;
import com.paq.repository.CourseRepository;
import jakarta.persistence.NoResultException;
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

/**
 *
 * @author Admin
 */
@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public List<Course> getCourses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "FROM Course c WHERE c.isDeleted = false";

        if (params != null) {
            if (params.containsKey("keyword")) {
                hql += " AND c.name LIKE :kw";
            }
            if (params.containsKey("lecturerId")) {
                hql += " AND c.lecturerId.id = :lecturerId";
            }
            if (params.containsKey("subjectId")) {
                hql += " AND :subjectId MEMBER OF c.subjectSet";
            }
            if (params.containsKey("isPaid")) {
                hql += " AND c.isPaid = :isPaid";
            }
        }

        Query<Course> q = s.createQuery(hql, Course.class);

        if (params != null) {
            if (params.containsKey("keyword")) {
                q.setParameter("kw", "%" + params.get("keyword") + "%");
            }
            if (params.containsKey("lecturerId")) {
                q.setParameter("lecturerId", Integer.parseInt(params.get("lecturerId")));
            }
            if (params.containsKey("isPaid")) {
                q.setParameter("isPaid", Boolean.parseBoolean(params.get("isPaid")));
            }
        }

        int pageSize = this.env.getProperty("courses.page_size", Integer.class);
        int page = 1;
        if (params != null && params.containsKey("page")) {
            page = Integer.parseInt(params.get("page"));
        }
        q.setMaxResults(pageSize);
        q.setFirstResult((page - 1) * pageSize);

        return q.getResultList();
    }

    @Override
    public Course getCourseById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Course.class, id);
    }

    @Override
    public Course getCourseByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Course> q = s.createQuery(
                "FROM Course c WHERE c.name = :name AND c.isDeleted = false",
                Course.class
        );
        q.setParameter("name", name);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Course addOrUpdateCourse(Course course) {
        Session s = this.factory.getObject().getCurrentSession();
        if (course.getId() != null) {
            return s.merge(course);
        }
        s.persist(course);
        return course;
    }

    @Override
    public void deleteCourse(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Course course = s.get(Course.class, id);
        if (course != null) {
            course.setIsDeleted(Boolean.TRUE);
            s.merge(course);
        }
    }

    @Override
    public Long countCourses(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "SELECT COUNT(c) FROM Course c WHERE c.isDeleted = false";

        if (params != null) {
            if (params.containsKey("keyword")) {
                hql += " AND c.name LIKE :kw";
            }
            if (params.containsKey("lecturerId")) {
                hql += " AND c.lecturerId.id = :lecturerId";
            }
            if (params.containsKey("subjectId")) {
                hql += " AND :subjectId MEMBER OF c.subjectSet";
            }
            if (params.containsKey("isPaid")) {
                hql += " AND c.isPaid = :isPaid";
            }
        }

        Query<Long> q = s.createQuery(hql, Long.class);

        if (params != null) {
            if (params.containsKey("keyword")) {
                q.setParameter("kw", "%" + params.get("keyword") + "%");
            }
            if (params.containsKey("lecturerId")) {
                q.setParameter("lecturerId", Integer.parseInt(params.get("lecturerId")));
            }
            if (params.containsKey("isPaid")) {
                q.setParameter("isPaid", Boolean.parseBoolean(params.get("isPaid")));
            }
        }

        return q.getSingleResult();
    }
}
