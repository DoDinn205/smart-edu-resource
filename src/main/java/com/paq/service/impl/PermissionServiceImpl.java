package com.paq.service.impl;

import com.paq.pojo.Course;
import com.paq.pojo.Payment;
import com.paq.pojo.Quiz;
import com.paq.pojo.Resource;
import com.paq.pojo.User;
import com.paq.repository.CourseRepository;
import com.paq.repository.ResourceRepository;
import com.paq.repository.UserRepository;
import com.paq.service.PermissionService;
import com.paq.utils.constant.RoleEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public void requireAdmin() {
        if (!this.isAdmin(this.getCurrentUser())) {
            throw new PermissionException("Bạn không có quyền admin");
        }
    }

    @Override
    public void requireLecturerOrAdmin() {
        User user = this.getCurrentUser();
        if (!this.isAdmin(user) && !this.isLecturer(user)) {
            throw new PermissionException("Bạn không có quyền giảng viên hoặc admin");
        }
    }

    @Override
    public void requireStudent() {
        if (!this.isStudent(this.getCurrentUser())) {
            throw new PermissionException("Chỉ sinh viên mới có quyền thực hiện thao tác này");
        }
    }

    @Override
    public void requireCurrentUserOrAdmin(Integer userId) {
        User user = this.getCurrentUser();
        if (!this.isAdmin(user) && (userId == null || !userId.equals(user.getId()))) {
            throw new PermissionException("Bạn không có quyền thao tác với người dùng này");
        }
    }

    @Override
    public void requireResourceOwnerOrAdmin(Integer resourceId) {
        User user = this.getCurrentUser();
        Resource resource = this.resourceRepo.getResourceById(resourceId);
        if (resource == null) {
            throw new IdInvalidException("Resource không tồn tại");
        }

        if (!this.isAdmin(user) && !(this.isLecturer(user) && this.isOwner(user, resource.getUploadBy()))) {
            throw new PermissionException("Bạn không có quyền thao tác với học liệu này");
        }
    }

    @Override
    public void requireCourseLecturerOrAdmin(Integer courseId) {
        User user = this.getCurrentUser();
        Course course = this.courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        if (!this.isAdmin(user) && !this.isLecturer(user)) {
            throw new PermissionException("Bạn không có quyền thao tác với khóa học này");
        }
    }

    @Override
    public void requireEnrollmentOrAdmin(Integer courseId) {
        User user = this.getCurrentUser();
        Course course = this.courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        if (this.isAdmin(user)) {
            return;
        }

        if (!this.isStudent(user) || !this.existsEnrollment(courseId, user.getId())) {
            throw new PermissionException("Bạn chưa ghi danh khóa học này");
        }
    }

    @Override
    public void requirePaymentOwnerOrAdmin(Integer paymentId) {
        User user = this.getCurrentUser();
        Payment payment = this.getPaymentById(paymentId);
        if (payment == null) {
            throw new IdInvalidException("Payment không tồn tại");
        }

        User paymentOwner = payment.getEnrollmentId().getStudentId().getUserId();
        if (!this.isAdmin(user) && !this.isOwner(user, paymentOwner)) {
            throw new PermissionException("Bạn không có quyền thao tác với thanh toán này");
        }
    }

    @Override
    public void requireQuizOwnerOrAdmin(Integer quizId) {
        User user = this.getCurrentUser();
        Quiz quiz = this.getQuizById(quizId);
        if (quiz == null || Boolean.TRUE.equals(quiz.getIsDeleted())) {
            throw new IdInvalidException("Quiz không tồn tại");
        }

        if (!this.isAdmin(user) && !this.isOwner(user, quiz.getCreatedBy())) {
            throw new PermissionException("Bạn không có quyền thao tác với bài kiểm tra này");
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new PermissionException("Bạn chưa đăng nhập");
        }

        User user = this.userRepo.getUserByUsername(auth.getName());
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            throw new PermissionException("Tài khoản không hợp lệ");
        }

        return user;
    }

    private boolean isAdmin(User user) {
        return user != null && user.getRole() == RoleEnum.ADMIN;
    }

    private boolean isLecturer(User user) {
        return user != null && user.getRole() == RoleEnum.LECTURER;
    }

    private boolean isStudent(User user) {
        return user != null && user.getRole() == RoleEnum.STUDENT;
    }

    private boolean isOwner(User user, User owner) {
        return user != null && owner != null && user.getId().equals(owner.getId());
    }

    private boolean existsEnrollment(Integer courseId, Integer userId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> q = session.createQuery(
                "SELECT COUNT(e.id) FROM Enrollment e "
                + "WHERE e.courseId.id = :courseId "
                + "AND e.studentId.userId.id = :userId",
                Long.class);
        q.setParameter("courseId", courseId);
        q.setParameter("userId", userId);
        return q.getSingleResult() > 0;
    }

    private Payment getPaymentById(Integer paymentId) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Payment> q = session.createQuery(
                    "SELECT p FROM Payment p "
                    + "JOIN FETCH p.enrollmentId e "
                    + "JOIN FETCH e.studentId s "
                    + "JOIN FETCH s.userId "
                    + "WHERE p.id = :paymentId",
                    Payment.class);
            q.setParameter("paymentId", paymentId);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    private Quiz getQuizById(Integer quizId) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            Query<Quiz> q = session.createQuery(
                    "SELECT q FROM Quiz q "
                    + "JOIN FETCH q.createdBy "
                    + "WHERE q.id = :quizId",
                    Quiz.class);
            q.setParameter("quizId", quizId);
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
