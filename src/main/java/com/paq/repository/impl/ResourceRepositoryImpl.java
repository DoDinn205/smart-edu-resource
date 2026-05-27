/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository.impl;

import com.paq.pojo.Resource;
import com.paq.repository.ResourceRepository;
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
public class ResourceRepositoryImpl implements ResourceRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Resource> getResources(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();

        String hql = "FROM Resource r WHERE r.isDeleted = false";

        if (params != null && params.containsKey("keyword")) {

            if (params.containsKey("keyword")) {
                hql += " AND r.title LIKE :kw";
            }

            if (params.containsKey("level")) {
                hql += " AND r.level = :level";
            }
        }

        Query<Resource> q = s.createQuery(hql, Resource.class);

        if (params != null) {

            if (params.containsKey("keyword")) {
                q.setParameter("kw", "%" + params.get("keyword") + "%");
            }

            if (params.containsKey("level")) {
                q.setParameter("level", params.get("level"));
            }
        }

        return q.getResultList();
    }

    @Override
    public Resource getResourceById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Resource.class, id);
    }

    @Override
    public List<Resource> getRelatedResources(int resourceId) {
        Session s = this.factory.getObject().getCurrentSession();

        Query<Resource> q = s.createQuery(
                "FROM Resource r WHERE r.id != :id",
                Resource.class
        );

        q.setParameter("id", resourceId);

        q.setMaxResults(5);

        return q.getResultList();
    }

}
