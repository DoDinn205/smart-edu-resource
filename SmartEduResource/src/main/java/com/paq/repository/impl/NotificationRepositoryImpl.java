/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Notification;
import com.paq.repository.NotificationRepository;
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
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Notification> getNotificationsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Notification> q = s.createQuery(
                "FROM Notification n "
                + "WHERE n.userId.username = :username "
                + "AND (n.isDeleted = false OR n.isDeleted IS NULL) "
                + "ORDER BY n.createdAt DESC",
                Notification.class
        );

        q.setParameter("username", username);
        return q.getResultList();
    }

    @Override
    public Notification getNotificationById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Notification.class, id);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        Session s = this.factory.getObject().getCurrentSession();
        return (Notification) s.merge(notification);
    }
}
