package com.paq.repository.impl;

import com.paq.pojo.Question;
import com.paq.repository.QuestionRepository;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class QuestionRepositoryImpl implements QuestionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Question> getQuestionsByQuizId(int quizId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Question> q = session.createQuery(
                "SELECT DISTINCT q FROM Question q "
                + "LEFT JOIN FETCH q.answerOptionSet "
                + "WHERE q.quizId.id = :quizId "
                + "AND (q.isDeleted = false OR q.isDeleted IS NULL) "
                + "ORDER BY q.id ASC",
                Question.class);
        q.setParameter("quizId", quizId);
        return q.getResultList();
    }

    @Override
    public Question getQuestionById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Question> q = session.createQuery(
                    "SELECT DISTINCT q FROM Question q "
                    + "JOIN FETCH q.quizId qz "
                    + "LEFT JOIN FETCH q.answerOptionSet "
                    + "WHERE q.id = :id "
                    + "AND (q.isDeleted = false OR q.isDeleted IS NULL) "
                    + "AND (qz.isDeleted = false OR qz.isDeleted IS NULL)",
                    Question.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Question addOrUpdateQuestion(Question question) {
        Session session = this.factory.getObject().getCurrentSession();
        if (question.getId() != null) {
            return session.merge(question);
        }

        session.persist(question);
        return question;
    }

    @Override
    public void deleteQuestion(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Question question = session.get(Question.class, id);
        question.setIsDeleted(Boolean.TRUE);
        session.merge(question);
    }
}
