package com.paq.pojo.response;

import java.util.Date;
import java.util.List;

public class ResQuizAttemptDTO {

    private Integer id;
    private Date startedAt;
    private Date submittedAt;
    private Double score;
    private Double totalScore;
    private String status;
    private Integer quizId;
    private String quizTitle;
    private Integer courseId;
    private String courseName;
    private Integer studentId;
    private String studentCode;
    private ResUserDTO studentUser;
    private List<ResStudentAnswerResultDTO> answers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
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

    public List<ResStudentAnswerResultDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ResStudentAnswerResultDTO> answers) {
        this.answers = answers;
    }
}
