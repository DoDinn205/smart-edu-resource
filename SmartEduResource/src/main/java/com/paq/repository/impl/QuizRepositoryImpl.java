/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.StudentAnswer;
import com.paq.repository.QuizRepository;
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
public class QuizRepositoryImpl implements QuizRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Quiz> getQuizzes() {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Quiz> q = s.createQuery(
                "FROM Quiz q WHERE q.isDeleted = false",
                Quiz.class
        );

        return q.getResultList();
    }

    @Override
    public List<Quiz> getQuizzes(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "FROM Quiz q WHERE q.isDeleted = false";

        if (params != null) {
            if (params.containsKey("keyword")) {
                hql += " AND q.title LIKE :kw";
            }
            if (params.containsKey("courseId")) {
                hql += " AND q.courseId.id = :courseId";
            }
        }

        Query<Quiz> q = s.createQuery(hql, Quiz.class);

        if (params != null) {
            if (params.containsKey("keyword")) {
                q.setParameter("kw", "%" + params.get("keyword") + "%");
            }
            if (params.containsKey("courseId")) {
                q.setParameter("courseId", Integer.parseInt(params.get("courseId")));
            }
        }

        return q.getResultList();
    }

    @Override
    public Quiz getQuizById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Quiz> q = s.createQuery(
                "FROM Quiz q WHERE q.id = :id AND q.isDeleted = false",
                Quiz.class
        );

        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Question> getQuestionsByQuizId(int quizId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Question> q = s.createQuery(
                "FROM Question q WHERE q.quizId.id = :quizId AND q.isDeleted = false",
                Question.class
        );

        q.setParameter("quizId", quizId);

        return q.getResultList();
    }

    @Override
    public List<AnswerOption> getOptionsByQuestionId(int questionId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<AnswerOption> q = s.createQuery(
                "FROM AnswerOption a WHERE a.questionId.id = :questionId AND a.isDeleted = false",
                AnswerOption.class
        );

        q.setParameter("questionId", questionId);

        return q.getResultList();
    }

    @Override
    public AnswerOption getAnswerOptionById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<AnswerOption> q = s.createQuery(
                "FROM AnswerOption a WHERE a.id = :id AND a.isDeleted = false",
                AnswerOption.class
        );

        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
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
                "FROM QuizAttempt a "
                + "WHERE a.studentId.userId.username = :username "
                + "ORDER BY a.submittedAt DESC",
                QuizAttempt.class
        );

        q.setParameter("username", username);

        return q.getResultList();
    }

    @Override
    public Quiz addOrUpdateQuiz(Quiz quiz) {
        Session s = this.factory.getObject().getCurrentSession();
        if (quiz.getId() != null) {
            return s.merge(quiz);
        }
        s.persist(quiz);
        return quiz;
    }

    @Override
    public void deleteQuiz(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Quiz quiz = s.get(Quiz.class, id);
        if (quiz != null) {
            quiz.setIsDeleted(Boolean.TRUE);
            s.merge(quiz);
        }
    }

}
