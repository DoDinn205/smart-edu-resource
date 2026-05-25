package com.paq.repository.impl;

import com.paq.pojo.AnswerOption;
import com.paq.repository.AnswerOptionRepository;
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
public class AnswerOptionRepositoryImpl implements AnswerOptionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<AnswerOption> getAnswersByQuestionId(int questionId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<AnswerOption> q = session.createQuery(
                "SELECT a FROM AnswerOption a "
                + "WHERE a.questionId.id = :questionId "
                + "AND (a.isDeleted = false OR a.isDeleted IS NULL) "
                + "ORDER BY a.id ASC",
                AnswerOption.class);
        q.setParameter("questionId", questionId);
        return q.getResultList();
    }

    @Override
    public AnswerOption getAnswerById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<AnswerOption> q = session.createQuery(
                    "SELECT a FROM AnswerOption a "
                    + "JOIN FETCH a.questionId q "
                    + "JOIN FETCH q.quizId qz "
                    + "WHERE a.id = :id "
                    + "AND (a.isDeleted = false OR a.isDeleted IS NULL) "
                    + "AND (q.isDeleted = false OR q.isDeleted IS NULL) "
                    + "AND (qz.isDeleted = false OR qz.isDeleted IS NULL)",
                    AnswerOption.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public AnswerOption addOrUpdateAnswer(AnswerOption answer) {
        Session session = this.factory.getObject().getCurrentSession();
        if (answer.getId() != null) {
            return session.merge(answer);
        }

        session.persist(answer);
        return answer;
    }

    @Override
    public void deleteAnswer(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        AnswerOption answer = session.get(AnswerOption.class, id);
        answer.setIsDeleted(Boolean.TRUE);
        session.merge(answer);
    }
}
