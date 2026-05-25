package com.paq.pojo.request;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paq.utils.constant.LevelEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReqCourseDTO {

    @NotBlank(message = "Name không được để trống")
    @Size(max = 255, message = "Name tối đa 255 ký tự")
    private String name;

    @Size(max = 65535, message = "Description tối đa 65535 ký tự")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Boolean isPaid;

    private LevelEnum targetLevel;

    private Set<Integer> subjectIds;

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

    public LevelEnum getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(LevelEnum targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Set<Integer> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(Set<Integer> subjectIds) {
        this.subjectIds = subjectIds;
    }
}
