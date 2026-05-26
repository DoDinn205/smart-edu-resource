package com.paq.pojo.response;

import java.util.Date;
import java.util.List;

public class ResCourseDTO {

    private Integer id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean isPaid;
    private Boolean isDeleted;
    private String targetLevel;
    private ResUserDTO createdBy;
    private Integer lecturerId;
    private ResUserDTO lecturerUser;
    private List<ResSubjectDTO> subjects;
    private Integer enrollmentCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(String targetLevel) {
        this.targetLevel = targetLevel;
    }

    public ResUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ResUserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

    public ResUserDTO getLecturerUser() {
        return lecturerUser;
    }

    public void setLecturerUser(ResUserDTO lecturerUser) {
        this.lecturerUser = lecturerUser;
    }

    public List<ResSubjectDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<ResSubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Integer getEnrollmentCount() {
        return enrollmentCount;
    }

    public void setEnrollmentCount(Integer enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }
}
