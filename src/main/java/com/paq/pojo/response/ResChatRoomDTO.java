package com.paq.pojo.response;

import java.util.Date;

public class ResChatRoomDTO {

    private Integer id;
    private String type;
    private String name;
    private Date createdAt;
    private Integer courseId;
    private String courseName;
    private ResUserDTO createdBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ResUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ResUserDTO createdBy) {
        this.createdBy = createdBy;
    }
}
