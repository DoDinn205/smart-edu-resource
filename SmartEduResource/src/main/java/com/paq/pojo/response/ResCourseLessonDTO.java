package com.paq.pojo.response;

public class ResCourseLessonDTO {

    private Integer id;
    private String title;
    private Integer chapterNum;
    private Integer lessonNum;
    private Boolean isFree;

    // Resource info (null if quiz lesson)
    private Integer resourceId;
    private String resourceTitle;
    private String fileUrl;
    private String thumbnailUrl;
    private String format;         // MP4, PDF, PPTX, DOCX...
    private Integer pageCount;

    // Quiz info (null if resource lesson)
    private Integer quizId;
    private String quizTitle;
    private Integer durationMinutes;
    private Integer questionCount;

    // Derived display type: VIDEO | DOCUMENT | QUIZ
    private String itemType;

    public ResCourseLessonDTO() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getChapterNum() { return chapterNum; }
    public void setChapterNum(Integer chapterNum) { this.chapterNum = chapterNum; }

    public Integer getLessonNum() { return lessonNum; }
    public void setLessonNum(Integer lessonNum) { this.lessonNum = lessonNum; }

    public Boolean getIsFree() { return isFree; }
    public void setIsFree(Boolean isFree) { this.isFree = isFree; }

    public Integer getResourceId() { return resourceId; }
    public void setResourceId(Integer resourceId) { this.resourceId = resourceId; }

    public String getResourceTitle() { return resourceTitle; }
    public void setResourceTitle(String resourceTitle) { this.resourceTitle = resourceTitle; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }
}
