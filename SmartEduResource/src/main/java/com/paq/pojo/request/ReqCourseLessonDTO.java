package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReqCourseLessonDTO {

    @NotNull(message = "courseId khong duoc de trong")
    private Integer courseId;

    @NotBlank(message = "title khong duoc de trong")
    @Size(max = 255, message = "title toi da 255 ky tu")
    private String title;

    @NotNull(message = "chapterNum khong duoc de trong")
    private Integer chapterNum;

    @NotNull(message = "lessonNum khong duoc de trong")
    private Integer lessonNum;

    private Integer resourceId;   // nullable — resource lesson
    private Integer quizId;       // nullable — quiz lesson
    private Boolean isFree = false;

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getChapterNum() { return chapterNum; }
    public void setChapterNum(Integer chapterNum) { this.chapterNum = chapterNum; }

    public Integer getLessonNum() { return lessonNum; }
    public void setLessonNum(Integer lessonNum) { this.lessonNum = lessonNum; }

    public Integer getResourceId() { return resourceId; }
    public void setResourceId(Integer resourceId) { this.resourceId = resourceId; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public Boolean getIsFree() { return isFree; }
    public void setIsFree(Boolean isFree) { this.isFree = isFree; }
}
