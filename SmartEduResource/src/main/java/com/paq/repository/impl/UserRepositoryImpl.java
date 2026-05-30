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

import com.paq.pojo.Lecturer;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import com.paq.repository.UserRepository;
import com.paq.utils.constant.RoleEnum;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@Transactional
@PropertySource("classpath:configs.properties")
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root<User> root = q.from(User.class);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(root.get("fullName")), like),
                        b.like(b.lower(root.get("username")), like),
                        b.like(b.lower(root.get("email")), like),
                        b.like(b.lower(root.get("phone")), like)));
            }

            String role = params.get("role");
            if (role != null && !role.isBlank()) {
                predicates.add(b.equal(root.get("role"), RoleEnum.valueOf(role.trim().toUpperCase())));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.valueOf(isActive)));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }
        q.orderBy(b.desc(root.get("id")));

        Query<User> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("users.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public User getUserById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public User getUserByPhone(String phone) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<User> q = session.createNamedQuery("User.findByPhone", User.class);
            q.setParameter("phone", phone);

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<User> q = session.createNamedQuery("User.findByUsername", User.class);
            q.setParameter("username", username);

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<User> q = session.createNamedQuery("User.findByEmail", User.class);
            q.setParameter("email", email);

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Student> getStudents(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Student> q = b.createQuery(Student.class);
        Root<Student> root = q.from(Student.class);
        root.fetch("userId", JoinType.INNER);
        Join<Student, User> user = root.join("userId", JoinType.INNER);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(user.get("role"), RoleEnum.STUDENT));
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(user.get("fullName")), like),
                        b.like(b.lower(user.get("username")), like),
                        b.like(b.lower(user.get("email")), like),
                        b.like(b.lower(user.get("phone")), like)));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(user.get("isActive"), Boolean.valueOf(isActive)));
            }

            String studentCode = params.get("studentCode");
            if (studentCode != null && !studentCode.isBlank()) {
                predicates.add(b.like(b.lower(root.get("studentCode")),
                        String.format("%%%s%%", studentCode.trim().toLowerCase())));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Student> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("users.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Student getStudentById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Student> q = session.createQuery(
                    "SELECT s FROM Student s JOIN FETCH s.userId WHERE s.id = :id",
                    Student.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Student getStudentByUserId(int userId) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Student> q = session.createQuery(
                    "SELECT s FROM Student s JOIN FETCH s.userId WHERE s.userId.id = :userId",
                    Student.class);
            q.setParameter("userId", userId);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Student addOrUpdateStudent(Student student) {
        Session session = this.factory.getObject().getCurrentSession();
        if (student.getId() != null) {
            return session.merge(student);
        }

        session.persist(student);
        return student;
    }

    @Override
    public List<Lecturer> getLecturers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Lecturer> q = b.createQuery(Lecturer.class);
        Root<Lecturer> root = q.from(Lecturer.class);
        root.fetch("userId", JoinType.INNER);
        Join<Lecturer, User> user = root.join("userId", JoinType.INNER);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(user.get("role"), RoleEnum.LECTURER));
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(user.get("fullName")), like),
                        b.like(b.lower(user.get("username")), like),
                        b.like(b.lower(user.get("email")), like),
                        b.like(b.lower(user.get("phone")), like)));
            }

            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(user.get("isActive"), Boolean.valueOf(isActive)));
            }

            String isApprove = params.get("isApprove");
            if (isApprove != null && !isApprove.isBlank()) {
                predicates.add(b.equal(root.get("isApprove"), Boolean.valueOf(isApprove)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Lecturer> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("users.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Lecturer getLecturerById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Lecturer> q = session.createQuery(
                    "SELECT l FROM Lecturer l JOIN FETCH l.userId WHERE l.id = :id",
                    Lecturer.class);
            q.setParameter("id", id);

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Lecturer getLecturerByUserId(int userId) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Lecturer> q = session.createQuery(
                    "SELECT l FROM Lecturer l JOIN FETCH l.userId WHERE l.userId.id = :userId",
                    Lecturer.class);
            q.setParameter("userId", userId);

            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Lecturer addOrUpdateLecturer(Lecturer lecturer) {
        Session session = this.factory.getObject().getCurrentSession();
        if (lecturer.getId() != null) {
            return session.merge(lecturer);
        }

        session.persist(lecturer);
        return lecturer;
    }

    @Override
    public User addUser(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.merge(user);
    }

    @Override
    public Long countUsers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<User> root = q.from(User.class);
        q.select(b.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(root.get("fullName")), like),
                        b.like(b.lower(root.get("username")), like),
                        b.like(b.lower(root.get("email")), like),
                        b.like(b.lower(root.get("phone")), like)));
            }
            String role = params.get("role");
            if (role != null && !role.isBlank()) {
                predicates.add(b.equal(root.get("role"), RoleEnum.valueOf(role.trim().toUpperCase())));
            }
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(root.get("isActive"), Boolean.valueOf(isActive)));
            }
        }
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        return session.createQuery(q).getSingleResult();
    }

    @Override
    public Long countStudents(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Student> root = q.from(Student.class);
        Join<Student, User> user = root.join("userId", JoinType.INNER);
        q.select(b.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(user.get("role"), RoleEnum.STUDENT));
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(user.get("fullName")), like),
                        b.like(b.lower(user.get("username")), like),
                        b.like(b.lower(user.get("email")), like),
                        b.like(b.lower(user.get("phone")), like)));
            }
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(user.get("isActive"), Boolean.valueOf(isActive)));
            }
            String studentCode = params.get("studentCode");
            if (studentCode != null && !studentCode.isBlank()) {
                predicates.add(b.like(b.lower(root.get("studentCode")),
                        String.format("%%%s%%", studentCode.trim().toLowerCase())));
            }
        }
        q.where(predicates.toArray(Predicate[]::new));

        return session.createQuery(q).getSingleResult();
    }

    @Override
    public Long countLecturers(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Lecturer> root = q.from(Lecturer.class);
        Join<Lecturer, User> user = root.join("userId", JoinType.INNER);
        q.select(b.countDistinct(root));

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(b.equal(user.get("role"), RoleEnum.LECTURER));
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                String like = String.format("%%%s%%", kw.trim().toLowerCase());
                predicates.add(b.or(
                        b.like(b.lower(user.get("fullName")), like),
                        b.like(b.lower(user.get("username")), like),
                        b.like(b.lower(user.get("email")), like),
                        b.like(b.lower(user.get("phone")), like)));
            }
            String isActive = params.get("isActive");
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(b.equal(user.get("isActive"), Boolean.valueOf(isActive)));
            }
            String isApprove = params.get("isApprove");
            if (isApprove != null && !isApprove.isBlank()) {
                predicates.add(b.equal(root.get("isApprove"), Boolean.valueOf(isApprove)));
            }
        }
        q.where(predicates.toArray(Predicate[]::new));

        return session.createQuery(q).getSingleResult();
    }

}
