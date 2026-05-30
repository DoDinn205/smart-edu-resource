package com.paq.repository.impl;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.StudentAnswer;
import com.paq.repository.QuizRepository;
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
public class QuizRepositoryImpl implements QuizRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Quiz> getQuizzes(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Quiz> q = b.createQuery(Quiz.class);
        Root<Quiz> root = q.from(Quiz.class);
        root.fetch("courseId", JoinType.LEFT);
        root.fetch("createdBy", JoinType.LEFT);
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

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.parseInt(courseId)));
            }

            String createdById = params.get("createdById");
            if (createdById != null && !createdById.isEmpty()) {
                predicates.add(b.equal(root.get("createdBy").get("id"), Integer.parseInt(createdById)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Quiz> query = session.createQuery(q);

        if (params != null) {
            int pageSize = this.env.getProperty("quizzes.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;
            query.setMaxResults(pageSize);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Quiz getQuizById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Quiz> q = session.createQuery(
                    "SELECT DISTINCT qz FROM Quiz qz "
                    + "JOIN FETCH qz.courseId c "
                    + "JOIN FETCH qz.createdBy "
                    + "LEFT JOIN FETCH qz.questionSet qs "
                    + "LEFT JOIN FETCH qs.answerOptionSet "
                    + "WHERE qz.id = :id "
                    + "AND (qz.isDeleted = false OR qz.isDeleted IS NULL) "
                    + "AND (c.isDeleted = false OR c.isDeleted IS NULL)",
                    Quiz.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Quiz addOrUpdateQuiz(Quiz quiz) {
        Session session = this.factory.getObject().getCurrentSession();
        if (quiz.getId() != null) {
            return session.merge(quiz);
        }

        session.persist(quiz);
        return quiz;
    }

    @Override
    public void deleteQuiz(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Quiz quiz = session.get(Quiz.class, id);
        quiz.setIsDeleted(Boolean.TRUE);
        session.merge(quiz);
    }

    @Override
    public List<Question> getQuestionsByQuizId(int quizId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Question> q = s.createQuery(
                "FROM Question q WHERE q.quizId.id = :quizId AND (q.isDeleted = false OR q.isDeleted IS NULL)",
                Question.class
        );

        q.setParameter("quizId", quizId);
        return q.getResultList();
    }

    @Override
    public List<AnswerOption> getOptionsByQuestionId(int questionId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<AnswerOption> q = s.createQuery(
                "FROM AnswerOption a WHERE a.questionId.id = :questionId AND (a.isDeleted = false OR a.isDeleted IS NULL)",
                AnswerOption.class
        );

        q.setParameter("questionId", questionId);
        return q.getResultList();
    }

    @Override
    public AnswerOption getAnswerOptionById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(AnswerOption.class, id);
    }

    @Override
    public QuizAttempt addQuizAttempt(QuizAttempt attempt) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(attempt);
        return attempt;
    }

    @Override
    public StudentAnswer addStudentAnswer(StudentAnswer answer) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(answer);
        return answer;
    }

    @Override
    public List<QuizAttempt> getAttemptsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<QuizAttempt> q = s.createQuery(
                "FROM QuizAttempt a WHERE a.studentId.userId.username = :username",
                QuizAttempt.class
        );

        q.setParameter("username", username);
        return q.getResultList();
    }
}
