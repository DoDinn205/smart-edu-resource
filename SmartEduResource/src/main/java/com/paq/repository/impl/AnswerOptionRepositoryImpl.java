package com.paq.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.response.ResAnswerOptionDTO;
import com.paq.repository.AnswerOptionRepository;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

@Repository
@Transactional
public class AnswerOptionRepositoryImpl implements AnswerOptionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ResAnswerOptionDTO> getAnswersByQuestionId(int questionId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ResAnswerOptionDTO> q = b.createQuery(ResAnswerOptionDTO.class);
        Root<AnswerOption> root = q.from(AnswerOption.class);
        Join<AnswerOption, Question> question = root.join("questionId", JoinType.INNER);
        Join<Question, Quiz> quiz = question.join("quizId", JoinType.INNER);

        q.select(b.construct(ResAnswerOptionDTO.class,
                root.get("id"),
                root.get("content"),
                root.get("isCorrect")))
                .where(b.and(
                        b.equal(question.get("id"), questionId),
                        b.or(b.isFalse(root.get("isDeleted")), b.isNull(root.get("isDeleted"))),
                        b.or(b.isFalse(question.get("isDeleted")), b.isNull(question.get("isDeleted"))),
                        b.or(b.isFalse(quiz.get("isDeleted")), b.isNull(quiz.get("isDeleted")))))
                .orderBy(b.asc(root.get("id")));

        return session.createQuery(q).getResultList();
    }

    @Override
    public AnswerOption getAnswerById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<AnswerOption> q = session.createQuery(
                    "SELECT DISTINCT a FROM AnswerOption a "
                    + "JOIN FETCH a.questionId q "
                    + "JOIN FETCH q.quizId qz "
                    + "LEFT JOIN FETCH q.answerOptionSet "
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
