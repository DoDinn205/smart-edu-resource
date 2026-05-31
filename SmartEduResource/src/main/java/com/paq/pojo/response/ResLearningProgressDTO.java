package com.paq.pojo.response;

import java.util.Date;
import java.util.List;

public class ResLearningProgressDTO {

    private Integer enrollmentId;
    private Date enrollDate;
    private Double overallProgress;
    private String status;
    private Integer totalStudyTime;
    private Integer courseId;
    private String courseName;
    private Integer studentId;
    private String studentCode;
    private ResUserDTO studentUser;
    private String lecturerFeedback;
    private List<ResQuizAttemptDTO> quizAttempts;

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public Double getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(Double overallProgress) {
        this.overallProgress = overallProgress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Integer totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public ResUserDTO getStudentUser() {
        return studentUser;
    }

    public void setStudentUser(ResUserDTO studentUser) {
        this.studentUser = studentUser;
    }

    public String getLecturerFeedback() {
        return lecturerFeedback;
    }

    public void setLecturerFeedback(String lecturerFeedback) {
        this.lecturerFeedback = lecturerFeedback;
    }

    public List<ResQuizAttemptDTO> getQuizAttempts() {
        return quizAttempts;
    }

    public void setQuizAttempts(List<ResQuizAttemptDTO> quizAttempts) {
        this.quizAttempts = quizAttempts;
    }
}
