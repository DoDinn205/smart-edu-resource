package com.paq.repository.impl;

import com.paq.pojo.Payment;
import com.paq.repository.PaymentRepository;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Payment getPaymentById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Payment> q = session.createQuery(
                    "SELECT p FROM Payment p "
                    + "JOIN FETCH p.enrollmentId e "
                    + "JOIN FETCH e.studentId s "
                    + "JOIN FETCH s.userId "
                    + "WHERE p.id = :id",
                    Payment.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
