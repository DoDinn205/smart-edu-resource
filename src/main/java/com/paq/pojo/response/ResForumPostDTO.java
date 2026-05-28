package com.paq.pojo.response;

import java.util.Date;

public class ResForumPostDTO {

    private Integer id;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Integer threadId;
    private ResUserDTO user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public ResUserDTO getUser() {
        return user;
    }

    public void setUser(ResUserDTO user) {
        this.user = user;
    }
}
