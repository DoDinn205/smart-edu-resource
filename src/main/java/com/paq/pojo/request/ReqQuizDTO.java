package com.paq.pojo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReqQuizDTO {

    @NotBlank(message = "Title khong duoc de trong")
    @Size(max = 255, message = "Title toi da 255 ky tu")
    private String title;

    private String description;

    @Min(value = 1, message = "Duration minutes phai lon hon 0")
    private Integer durationMinutes;

    @NotNull(message = "Course id khong duoc de trong")
    private Integer courseId;

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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
