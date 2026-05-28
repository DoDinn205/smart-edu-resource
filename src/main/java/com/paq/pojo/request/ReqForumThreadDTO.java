package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReqForumThreadDTO {

    @NotBlank(message = "Title không được để trống")
    @Size(max = 255, message = "Title tối đa 255 ký tự")
    private String title;

    @Size(max = 65535, message = "Content tối đa 65535 ký tự")
    private String content;

    @NotNull(message = "Category id không được để trống")
    private Integer categoryId;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
