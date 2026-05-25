package com.paq.pojo.response;

import java.util.Date;
import java.util.List;

public class ResQuizDTO {

    private Integer id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private Double totalScore;
    private Date createdAt;
    private Integer courseId;
    private ResUserDTO createdBy;
    private List<ResQuestionDTO> questions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public ResUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ResUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public List<ResQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ResQuestionDTO> questions) {
        this.questions = questions;
    }
}
