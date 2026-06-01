package com.paq.repository;

import com.paq.utils.constant.PaymentStatusEnum;
import com.paq.utils.constant.RoleEnum;

public interface DashboardRepository {

    long countActiveUsers();

    long countUsersByRole(RoleEnum role);

    long countStudents();

    long countLecturers();

    long countPendingLecturers();

    long countCourses();

    long countResources();

    long countQuizzes();

    long countEnrollments();

    long countQuizAttempts();

    double getAverageLearningProgress();

    long getTotalStudyTime();

    double getAverageQuizScore();

    long getTotalRevenue();

    long countPaymentsByStatus(PaymentStatusEnum status);

    long countLecturerCourses(int userId);

    long countLecturerStudents(int userId);

    long countLecturerResources(int userId);

    long countLecturerQuizzes(int userId);

    long countLecturerEnrollments(int userId);

    long countLecturerQuizAttempts(int userId);

    double getLecturerAverageLearningProgress(int userId);

    long getLecturerTotalStudyTime(int userId);

    double getLecturerAverageQuizScore(int userId);

    long getLecturerTotalRevenue(int userId);

    long countLecturerPaymentsByStatus(int userId, PaymentStatusEnum status);

    long countStudentEnrollments(String username);

    long countStudentCompletedResources(String username);

    long getStudentTotalStudyTime(String username);

    long countStudentQuizAttempts(String username);

    double getStudentAverageQuizScore(String username);

    long countStudentLearningLogs(String username);
}
