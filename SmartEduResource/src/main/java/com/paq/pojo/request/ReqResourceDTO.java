package com.paq.pojo.request;

import com.paq.utils.constant.FormatEnum;
import com.paq.utils.constant.LevelEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class ReqResourceDTO {

    @NotBlank(message = "Title khong duoc de trong")
    @Size(max = 255, message = "Title toi da 255 ky tu")
    private String title;

    private String description;

    @Size(max = 255, message = "File URL toi da 255 ky tu")
    private String fileUrl;

    @Size(max = 255, message = "Thumbnail URL toi da 255 ky tu")
    private String thumbnailUrl;

    private FormatEnum format;

    @Min(value = 0, message = "File size phai lon hon hoac bang 0")
    private Integer fileSize;

    private LevelEnum level;

    @Min(value = 0, message = "Page count phai lon hon hoac bang 0")
    private Integer pageCount;

    private Set<Integer> subjectIds;
    private Set<Integer> topicIds;
    private Set<Integer> tagIds;
    private Set<Integer> typeIds;
    private Set<Integer> relatedResourceIds;

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public FormatEnum getFormat() {
        return format;
    }

    public void setFormat(FormatEnum format) {
        this.format = format;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Set<Integer> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(Set<Integer> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public Set<Integer> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(Set<Integer> topicIds) {
        this.topicIds = topicIds;
    }

    public Set<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Set<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(Set<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public Set<Integer> getRelatedResourceIds() {
        return relatedResourceIds;
    }

    public void setRelatedResourceIds(Set<Integer> relatedResourceIds) {
        this.relatedResourceIds = relatedResourceIds;
    }
}
