package com.paq.repository.impl;

import com.paq.pojo.Enrollment;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.Student;
import com.paq.repository.LearningResultRepository;
import com.paq.utils.constant.AttemptStatusEnum;
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
public class LearningResultRepositoryImpl implements LearningResultRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<QuizAttempt> getQuizAttempts(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<QuizAttempt> q = b.createQuery(QuizAttempt.class);
        Root<QuizAttempt> root = q.from(QuizAttempt.class);
        root.fetch("quizId", JoinType.LEFT).fetch("courseId", JoinType.LEFT);
        root.fetch("studentId", JoinType.LEFT).fetch("userId", JoinType.LEFT);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String quizId = params.get("quizId");
            if (quizId != null && !quizId.isEmpty()) {
                predicates.add(b.equal(root.get("quizId").get("id"), Integer.parseInt(quizId)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("quizId").get("courseId").get("id"), Integer.parseInt(courseId)));
            }

            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isEmpty()) {
                predicates.add(b.equal(root.get("studentId").get("id"), Integer.parseInt(studentId)));
            }

            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(b.equal(root.get("status"), AttemptStatusEnum.valueOf(status)));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }
        q.orderBy(b.desc(root.get("id")));

        Query<QuizAttempt> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("quizAttempts.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public QuizAttempt getQuizAttemptById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<QuizAttempt> q = session.createQuery(
                    "SELECT DISTINCT qa FROM QuizAttempt qa "
                    + "JOIN FETCH qa.quizId qz "
                    + "JOIN FETCH qz.courseId "
                    + "JOIN FETCH qa.studentId s "
                    + "JOIN FETCH s.userId "
                    + "LEFT JOIN FETCH qa.studentAnswerSet sa "
                    + "LEFT JOIN FETCH sa.questionId ques "
                    + "LEFT JOIN FETCH ques.answerOptionSet "
                    + "LEFT JOIN FETCH sa.optionId "
                    + "WHERE qa.id = :id",
                    QuizAttempt.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Enrollment> getLearningProgressByCourseId(int courseId, Map<String, String> params) {
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
            String studentId = params.get("studentId");
            if (studentId != null && !studentId.isEmpty()) {
                predicates.add(b.equal(root.get("studentId").get("id"), Integer.parseInt(studentId)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Enrollment> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("enrollments.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
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
}
