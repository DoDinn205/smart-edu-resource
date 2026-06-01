/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.Enrollment;
import com.paq.pojo.CourseLesson;
import com.paq.pojo.LearningLog;
import com.paq.pojo.Resource;
import com.paq.pojo.response.ResLearningLogDTO;
import com.paq.repository.EnrollmentRepository;
import com.paq.repository.CourseLessonRepository;
import com.paq.repository.LearningLogRepository;
import com.paq.repository.ResourceRepository;
import com.paq.service.StudentLearningService;
import com.paq.service.NotificationPublisherService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.EnrollmentStatusEnum;
import com.paq.utils.error.IdInvalidException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class StudentLearningServiceImpl implements StudentLearningService {

    @Autowired
    private LearningLogRepository learningLogRepo;

    @Autowired
    private ResourceRepository resourcementRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseLessonRepository lessonRepo;

    @Autowired
    private NotificationPublisherService notificationPublisher;

    @Override
    public ResLearningLogDTO startLearning(String username, int resourceId) {
        Resource resource = this.resourcementRepo.getResourceById(resourceId);

        if (resource == null || Boolean.TRUE.equals(resource.getIsDeleted())) {
            throw new IdInvalidException("Không tìm thấy học liệu!");
        }

        Enrollment enrollment = this.getEnrollmentForResource(username, resourceId);

        LearningLog log = new LearningLog();
        log.setResourceId(resource);
        log.setEnrollmentId(enrollment);
        log.setStartTime(new Date());
        log.setCompletionStatus(0);

        return DTOMapper.toLearningLogDTO(this.learningLogRepo.addLearningLog(log));
    }

    @Override
    public ResLearningLogDTO completeLearning(String username, int resourceId) {
        Resource resource = this.resourcementRepo.getResourceById(resourceId);

        if (resource == null || Boolean.TRUE.equals(resource.getIsDeleted())) {
            throw new IdInvalidException("Không tìm thấy học liệu!");
        }

        Enrollment enrollment = this.getEnrollmentForResource(username, resourceId);
        LearningLog completedLog = this.learningLogRepo.getCompletedLog(enrollment.getId(), resourceId);
        if (completedLog != null) {
            return DTOMapper.toLearningLogDTO(completedLog);
        }

        double previousProgress = enrollment.getOverallProgress() != null ? enrollment.getOverallProgress() : 0D;
        LearningLog log = new LearningLog();
        log.setResourceId(resource);
        log.setEnrollmentId(enrollment);
        log.setStartTime(new Date());
        log.setEndTime(new Date());
        log.setCompletionStatus(1);

        LearningLog savedLog = this.learningLogRepo.addLearningLog(log);
        double currentProgress = this.updateEnrollmentProgress(enrollment);
        Integer milestone = this.getReachedMilestone(previousProgress, currentProgress);
        if (milestone != null) {
            this.notificationPublisher.notifyProgressMilestone(enrollment, milestone);
        }

        return DTOMapper.toLearningLogDTO(savedLog);
    }

    @Override
    public List<ResLearningLogDTO> getHistory(String username) {
        return this.learningLogRepo.getLearningLogsByUsername(username)
                .stream()
                .map(l -> DTOMapper.toLearningLogDTO(l))
                .collect(Collectors.toList());
    }

    private Enrollment getEnrollmentForResource(String username, int resourceId) {
        List<CourseLesson> resourceLessons = this.lessonRepo.getLessonsByResourceId(resourceId);
        if (resourceLessons.isEmpty()) {
            throw new IllegalArgumentException("Học liệu chưa thuộc bài học nào!");
        }

        List<Enrollment> enrollments = this.enrollmentRepo.getEnrollmentsByUsername(username);
        return enrollments.stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatusEnum.SUCCESS)
                .filter(enrollment -> resourceLessons.stream()
                        .anyMatch(lesson -> lesson.getCourseId().getId().equals(enrollment.getCourseId().getId())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bạn chưa đăng ký khóa học chứa học liệu này!"));
    }

    private double updateEnrollmentProgress(Enrollment enrollment) {
        long totalResources = this.lessonRepo.countResourceLessonsByCourseId(enrollment.getCourseId().getId());
        long completedResources = this.learningLogRepo.countCompletedResourcesByEnrollmentId(enrollment.getId());
        double progress = totalResources == 0
                ? 0D
                : Math.min(100D, completedResources * 100D / totalResources);
        enrollment.setOverallProgress(progress);
        this.enrollmentRepo.addOrUpdateEnrollment(enrollment);
        return progress;
    }

    private Integer getReachedMilestone(double previousProgress, double currentProgress) {
        int[] milestones = {100, 75, 50, 25};
        for (int milestone : milestones) {
            if (previousProgress < milestone && currentProgress >= milestone) {
                return milestone;
            }
        }
        return null;
    }

}
