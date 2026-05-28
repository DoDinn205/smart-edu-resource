package com.paq.pojo.request;

import jakarta.validation.constraints.NotNull;

public class ReqPrivateChatRoomDTO {

    @NotNull(message = "Lecturer user id không được để trống")
    private Integer lecturerUserId;

    @NotNull(message = "Course id không được để trống")
    private Integer courseId;

    public Integer getLecturerUserId() {
        return lecturerUserId;
    }

    public void setLecturerUserId(Integer lecturerUserId) {
        this.lecturerUserId = lecturerUserId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
