package com.paq.service.impl;

import com.paq.pojo.User;
import com.paq.pojo.response.ResDashboardDTO;
import com.paq.pojo.response.ResLecturerDashboardDTO;
import com.paq.pojo.response.ResStudentDashboardDTO;
import com.paq.repository.DashboardRepository;
import com.paq.service.DashboardService;
import com.paq.service.PermissionService;
import com.paq.utils.constant.PaymentStatusEnum;
import com.paq.utils.constant.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public ResDashboardDTO getAdminDashboard() {
        this.permissionService.requireAdmin();

        ResDashboardDTO dto = new ResDashboardDTO();
        dto.setTotalUsers(this.dashboardRepo.countActiveUsers());
        dto.setTotalStudents(this.resolveStudentCount());
        dto.setTotalLecturers(this.resolveLecturerCount());
        dto.setPendingLecturers(this.dashboardRepo.countPendingLecturers());
        dto.setTotalCourses(this.dashboardRepo.countCourses());
        dto.setTotalResources(this.dashboardRepo.countResources());
        dto.setTotalQuizzes(this.dashboardRepo.countQuizzes());
        dto.setTotalEnrollments(this.dashboardRepo.countEnrollments());
        dto.setTotalQuizAttempts(this.dashboardRepo.countQuizAttempts());
        dto.setAverageLearningProgress(this.dashboardRepo.getAverageLearningProgress());
        dto.setTotalStudyTime(this.dashboardRepo.getTotalStudyTime());
        dto.setAverageQuizScore(this.dashboardRepo.getAverageQuizScore());
        dto.setTotalRevenue(this.dashboardRepo.getTotalRevenue());
        dto.setSuccessfulPayments(this.dashboardRepo.countPaymentsByStatus(PaymentStatusEnum.SUCCESS));
        dto.setPendingPayments(this.dashboardRepo.countPaymentsByStatus(PaymentStatusEnum.PENDING));

        return dto;
    }

    @Override
    public ResLecturerDashboardDTO getLecturerDashboard() {
        this.permissionService.requireLecturerOrAdmin();

        User currentUser = this.permissionService.getCurrentUser();
        int userId = currentUser.getId();

        ResLecturerDashboardDTO dto = new ResLecturerDashboardDTO();
        dto.setTotalStudents(this.dashboardRepo.countLecturerStudents(userId));
        dto.setTotalCourses(this.dashboardRepo.countLecturerCourses(userId));
        dto.setTotalResources(this.dashboardRepo.countLecturerResources(userId));
        dto.setTotalQuizzes(this.dashboardRepo.countLecturerQuizzes(userId));
        dto.setTotalEnrollments(this.dashboardRepo.countLecturerEnrollments(userId));
        dto.setTotalQuizAttempts(this.dashboardRepo.countLecturerQuizAttempts(userId));
        dto.setAverageLearningProgress(this.dashboardRepo.getLecturerAverageLearningProgress(userId));
        dto.setTotalStudyTime(this.dashboardRepo.getLecturerTotalStudyTime(userId));
        dto.setAverageQuizScore(this.dashboardRepo.getLecturerAverageQuizScore(userId));
        dto.setTotalRevenue(this.dashboardRepo.getLecturerTotalRevenue(userId));
        dto.setSuccessfulPayments(this.dashboardRepo.countLecturerPaymentsByStatus(userId, PaymentStatusEnum.SUCCESS));
        dto.setPendingPayments(this.dashboardRepo.countLecturerPaymentsByStatus(userId, PaymentStatusEnum.PENDING));

        return dto;
    }

    private long resolveStudentCount() {
        long studentRows = this.dashboardRepo.countStudents();
        return studentRows > 0 ? studentRows : this.dashboardRepo.countUsersByRole(RoleEnum.STUDENT);
    }

    private long resolveLecturerCount() {
        long lecturerRows = this.dashboardRepo.countLecturers();
        return lecturerRows > 0 ? lecturerRows : this.dashboardRepo.countUsersByRole(RoleEnum.LECTURER);
    }

    @Override
    public ResStudentDashboardDTO getStudentDashboard(String username) {
        ResStudentDashboardDTO dto = new ResStudentDashboardDTO();

        dto.setTotalEnrollments(
                this.dashboardRepo.countStudentEnrollments(username));

        dto.setCompletedResources(
                this.dashboardRepo.countStudentCompletedResources(username));

        dto.setTotalStudyTime(
                this.dashboardRepo.getStudentTotalStudyTime(username));

        dto.setTotalQuizAttempts(
                this.dashboardRepo.countStudentQuizAttempts(username));

        dto.setAverageQuizScore(
                this.dashboardRepo.getStudentAverageQuizScore(username));

        long totalLogs
                = this.dashboardRepo.countStudentLearningLogs(username);

        double progress = totalLogs == 0
                ? 0.0
                : dto.getCompletedResources() * 100.0 / totalLogs;

        dto.setLearningProgress(progress);

        return dto;
    }
}
