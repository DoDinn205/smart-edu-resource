/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Interaction;
import com.paq.repository.InteractionRepository;
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
                + "AND (i.isDeleted = false OR i.isDeleted IS NULL)",
                Interaction.class);

        q.setParameter("resourceId", resourceId);

        return q.getResultList();
    }

    @Override
    public Interaction getInteractionById(int id) {
        Session s = this.factory.getObject().getCurrentSession();

        return s.get(Interaction.class, id);
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

}
