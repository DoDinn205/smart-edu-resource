package com.paq.service;

import com.paq.pojo.Enrollment;
import com.paq.pojo.User;

public interface NotificationPublisherService {

    void notifyUser(User user, String title, String content);

    void notifyCourseStudents(int courseId, String title, String content);

    void notifyProgressMilestone(Enrollment enrollment, int milestone);
}
