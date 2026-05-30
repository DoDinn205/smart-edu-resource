/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.LearningLog;
import com.paq.repository.LearningLogRepository;
import java.util.List;
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
public class LearningLogRepositoryImpl implements LearningLogRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public LearningLog addLearningLog(LearningLog log) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(log);
        return log;
    }

    @Override
    public List<LearningLog> getLearningLogsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<LearningLog> q = s.createQuery(
                "FROM LearningLog l "
                + "WHERE l.enrollmentId.studentId.userId.username = :username",
                LearningLog.class
        );

        q.setParameter("username", username);
        return q.getResultList();
    }
}
