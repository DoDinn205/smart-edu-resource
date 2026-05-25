package com.paq.service;

public interface PermissionService {

    void requireAdmin();

    void requireLecturerOrAdmin();

    void requireStudent();

    void requireCurrentUserOrAdmin(Integer userId);

    void requireResourceOwnerOrAdmin(Integer resourceId);

    void requireCourseLecturerOrAdmin(Integer courseId);

    void requireEnrollmentOrAdmin(Integer courseId);

    void requirePaymentOwnerOrAdmin(Integer paymentId);

    void requireQuizOwnerOrAdmin(Integer quizId);
}
