package com.paq.repository.impl;

import com.paq.pojo.CourseLesson;
import com.paq.repository.CourseLessonRepository;
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
public class CourseLessonRepositoryImpl implements CourseLessonRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<CourseLesson> getLessonsByCourseId(int courseId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<CourseLesson> q = session.createNamedQuery("CourseLesson.findByCourse", CourseLesson.class);
        q.setParameter("courseId", courseId);
        return q.getResultList();
    }

    @Override
    public CourseLesson getLessonById(int id) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<CourseLesson> q = session.createQuery(
                    "SELECT cl FROM CourseLesson cl "
                    + "LEFT JOIN FETCH cl.resourceId "
                    + "LEFT JOIN FETCH cl.quizId "
                    + "WHERE cl.id = :id "
                    + "AND (cl.isDeleted = false OR cl.isDeleted IS NULL)",
                    CourseLesson.class);
            q.setParameter("id", id);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void addLesson(CourseLesson lesson) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(lesson);
    }

    @Override
    public void updateLesson(CourseLesson lesson) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(lesson);
    }

    @Override
    public void deleteLesson(int id) {
        CourseLesson lesson = this.getLessonById(id);
        if (lesson != null) {
            lesson.setIsDeleted(true);
            Session session = this.factory.getObject().getCurrentSession();
            session.merge(lesson);
        }
    }

    @Override
    public boolean hasActiveEnrollment(int courseId, int studentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(e) FROM Enrollment e "
                + "WHERE e.courseId.id = :courseId "
                + "AND e.studentId.id = :studentId "
                + "AND e.status = com.paq.utils.constant.EnrollmentStatusEnum.ACTIVE",
                Long.class);
        q.setParameter("courseId", courseId);
        q.setParameter("studentId", studentId);
        return q.getSingleResult() > 0;
    }

    @Override
    public boolean hasSuccessfulPayment(int courseId, int studentId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(p) FROM Payment p "
                + "WHERE p.enrollmentId.courseId.id = :courseId "
                + "AND p.enrollmentId.studentId.id = :studentId "
                + "AND p.status = com.paq.utils.constant.PaymentStatusEnum.SUCCESS",
                Long.class);
        q.setParameter("courseId", courseId);
        q.setParameter("studentId", studentId);
        return q.getSingleResult() > 0;
    }

    @Override
    public boolean hasSuccessfulEnrollment(int courseId, Integer studentId) {
        if (studentId == null) return false;
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(e) FROM Enrollment e "
                + "WHERE e.courseId.id = :courseId "
                + "AND e.studentId.id = :studentId "
                + "AND e.status = com.paq.utils.constant.EnrollmentStatusEnum.SUCCESS",
                Long.class);
        q.setParameter("courseId", courseId);
        q.setParameter("studentId", studentId);
        return q.getSingleResult() > 0;
    }
}
