/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.response;

/**
 *
 * @author Admin
 */
public class ResStudentDashboardDTO {

    private Long totalEnrollments;
    private Long completedResources;
    private Long totalStudyTime;
    private Long totalQuizAttempts;
    private Double averageQuizScore;
    private Double learningProgress;

    public Long getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(Long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public Long getCompletedResources() {
        return completedResources;
    }

    public void setCompletedResources(Long completedResources) {
        this.completedResources = completedResources;
    }

    public Long getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Long totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public Long getTotalQuizAttempts() {
        return totalQuizAttempts;
    }

    public void setTotalQuizAttempts(Long totalQuizAttempts) {
        this.totalQuizAttempts = totalQuizAttempts;
    }

    public Double getAverageQuizScore() {
        return averageQuizScore;
    }

    public void setAverageQuizScore(Double averageQuizScore) {
        this.averageQuizScore = averageQuizScore;
    }

    public Double getLearningProgress() {
        return learningProgress;
    }

    public void setLearningProgress(Double learningProgress) {
        this.learningProgress = learningProgress;
    }
}
