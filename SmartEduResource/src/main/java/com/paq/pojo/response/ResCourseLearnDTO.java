package com.paq.pojo.response;

import java.util.List;

public class ResCourseLearnDTO {

    // Course info
    private Integer courseId;
    private String courseName;
    private String description;
    private String targetLevel;
    private Boolean isPaid;
    private String thumbnailUrl;

    // Instructor
    private String lecturerName;
    private String lecturerTitle;

    // Enrollment info
    private Boolean hasAccess;         // true if user can view all lessons
    private String enrollmentStatus;   // ACTIVE | null

    // Curriculum
    private List<ResCourseChapterDTO> chapters;
    private Integer totalLessons;
    private Integer totalChapters;

    public ResCourseLearnDTO() {
    }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTargetLevel() { return targetLevel; }
    public void setTargetLevel(String targetLevel) { this.targetLevel = targetLevel; }

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

    public String getLecturerTitle() { return lecturerTitle; }
    public void setLecturerTitle(String lecturerTitle) { this.lecturerTitle = lecturerTitle; }

    public Boolean getHasAccess() { return hasAccess; }
    public void setHasAccess(Boolean hasAccess) { this.hasAccess = hasAccess; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public List<ResCourseChapterDTO> getChapters() { return chapters; }
    public void setChapters(List<ResCourseChapterDTO> chapters) { this.chapters = chapters; }

    public Integer getTotalLessons() { return totalLessons; }
    public void setTotalLessons(Integer totalLessons) { this.totalLessons = totalLessons; }

    public Integer getTotalChapters() { return totalChapters; }
    public void setTotalChapters(Integer totalChapters) { this.totalChapters = totalChapters; }
}
