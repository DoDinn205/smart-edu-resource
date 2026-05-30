/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Interaction;
import com.paq.pojo.InteractionReply;
import com.paq.repository.InteractionRepository;
import jakarta.persistence.NoResultException;
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
public class InteractionRepositoryImpl implements InteractionRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Interaction> getInteractionsByResourceId(int resourceId) {

        Session s = this.factory.getObject().getCurrentSession();
        Query<Interaction> q = s.createQuery(
                "FROM Interaction i "
                + "WHERE i.resourceId.id = :resourceId "
                + "ORDER BY i.createdAt DESC",
                Interaction.class
        );

        q.setParameter("resourceId", resourceId);

        return q.getResultList();
    }

    @Override
    public Interaction getInteractionById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Interaction> q = s.createQuery(
                "FROM Interaction i WHERE i.id = :id AND i.isDeleted = false",
                Interaction.class
        );

        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Interaction addInteraction(Interaction interaction) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(interaction);
        return interaction;
    }

    @Override
    public Interaction updateInteraction(Interaction interaction) {
        Session s = this.factory.getObject().getCurrentSession();
        s.merge(interaction);
        return interaction;
    }

    @Override
    public List<InteractionReply> getRepliesByInteractionId(int interactionId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<InteractionReply> q = s.createQuery(
                "FROM InteractionReply r "
                + "WHERE r.interactionId.id = :interactionId "
                + "AND r.isDeleted = false "
                + "ORDER BY r.createdAt ASC",
                InteractionReply.class
        );

        q.setParameter("interactionId", interactionId);
        return q.getResultList();
    }

    @Override
    public InteractionReply getReplyById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<InteractionReply> q = s.createQuery(
                "FROM InteractionReply r WHERE r.id = :id AND r.isDeleted = false",
                InteractionReply.class
        );

        q.setParameter("id", id);

        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public InteractionReply addReply(InteractionReply reply) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(reply);
        return reply;
    }

    @Override
    public InteractionReply updateReply(InteractionReply reply) {
        Session s = this.factory.getObject().getCurrentSession();
        s.merge(reply);
        return reply;
    }

}
