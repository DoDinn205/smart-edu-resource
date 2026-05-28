package com.paq.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.ChatRoom;
import com.paq.repository.ChatRoomRepository;
import com.paq.utils.constant.ChatRoomTypeEnum;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    @Autowired
    private Environment env;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ChatRoom> getRooms(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<ChatRoom> q = b.createQuery(ChatRoom.class);
        Root<ChatRoom> root = q.from(ChatRoom.class);
        root.fetch("courseId", JoinType.LEFT);
        root.fetch("createdBy", JoinType.INNER);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String type = params.get("type");
            if (type != null && !type.isEmpty()) {
                predicates.add(b.equal(root.get("type"), ChatRoomTypeEnum.valueOf(type)));
            }

            String courseId = params.get("courseId");
            if (courseId != null && !courseId.isEmpty()) {
                predicates.add(b.equal(root.get("courseId").get("id"), Integer.parseInt(courseId)));
            }

            String createdBy = params.get("createdBy");
            if (createdBy != null && !createdBy.isEmpty()) {
                predicates.add(b.equal(root.get("createdBy").get("id"), Integer.parseInt(createdBy)));
            }
        }

        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }
        q.orderBy(b.desc(root.get("createdAt")), b.desc(root.get("id")));

        Query<ChatRoom> query = session.createQuery(q);
        if (params != null) {
            int pageSize = this.env.getProperty("chat_rooms.page_size", Integer.class, 10);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            query.setMaxResults(pageSize);
            query.setFirstResult((page - 1) * pageSize);
        }

        return query.getResultList();
    }

    @Override
    public ChatRoom getRoomById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<ChatRoom> q = session.createQuery(
                    "SELECT r FROM ChatRoom r "
                    + "LEFT JOIN FETCH r.courseId c "
                    + "LEFT JOIN FETCH c.lecturerId l "
                    + "LEFT JOIN FETCH l.userId "
                    + "JOIN FETCH r.createdBy "
                    + "WHERE r.id = :id",
                    ChatRoom.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ChatRoom addOrUpdateRoom(ChatRoom room) {
        Session session = this.factory.getObject().getCurrentSession();
        if (room.getId() != null) {
            return session.merge(room);
        }

        session.persist(room);
        return room;
    }

    @Override
    public void deleteRoom(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        ChatRoom room = session.get(ChatRoom.class, id);
        session.remove(room);
    }
}
