package com.paq.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.User;
import com.paq.repository.UserRepository;

import jakarta.persistence.NoResultException;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

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
    public User addUser(User user) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(user);

        return user;
    }
}
