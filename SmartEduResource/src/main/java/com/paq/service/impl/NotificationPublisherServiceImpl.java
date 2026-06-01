package com.paq.service.impl;

import com.paq.pojo.Enrollment;
import com.paq.pojo.Notification;
import com.paq.pojo.User;
import com.paq.repository.EnrollmentRepository;
import com.paq.repository.NotificationRepository;
import com.paq.service.NotificationPublisherService;
import com.paq.utils.constant.EnrollmentStatusEnum;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotificationPublisherServiceImpl implements NotificationPublisherService {

    @Autowired
    private NotificationRepository notificationRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Override
    public void notifyUser(User user, String title, String content) {
        if (user == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(user);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(Boolean.FALSE);
        notification.setIsDeleted(Boolean.FALSE);
        notification.setCreatedAt(new Date());
        this.notificationRepo.addNotification(notification);
    }

    @Override
    public void notifyCourseStudents(int courseId, String title, String content) {
        this.enrollmentRepo.getEnrollmentsByCourseId(courseId, null).stream()
                .filter(e -> e.getStatus() == EnrollmentStatusEnum.SUCCESS)
                .map(Enrollment::getStudentId)
                .filter(student -> student != null && student.getUserId() != null)
                .map(student -> student.getUserId())
                .forEach(user -> this.notifyUser(user, title, content));
    }

    @Override
    public void notifyProgressMilestone(Enrollment enrollment, int milestone) {
        String courseName = enrollment.getCourseId().getName();
        this.notifyUser(
                enrollment.getStudentId().getUserId(),
                "Cập nhật tiến độ học tập",
                "Bạn đã hoàn thành " + milestone + "% khóa học " + courseName
                        + ". Hãy tiếp tục học để duy trì tiến độ.");
    }
}
