package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReqForumPostDTO {

    @NotBlank(message = "Content không được để trống")
    @Size(max = 65535, message = "Content tối đa 65535 ký tự")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
