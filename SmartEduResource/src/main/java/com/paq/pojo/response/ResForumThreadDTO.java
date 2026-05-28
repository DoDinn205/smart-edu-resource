package com.paq.pojo.response;

import java.util.Date;

public class ResForumThreadDTO {

    private Integer id;
    private String title;
    private String content;
    private Boolean isLock;
    private Date createdAt;
    private Date updateAt;
    private ResForumCategoryDTO category;
    private ResUserDTO createdBy;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public ResForumCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ResForumCategoryDTO category) {
        this.category = category;
    }

    public ResUserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ResUserDTO createdBy) {
        this.createdBy = createdBy;
    }
}
