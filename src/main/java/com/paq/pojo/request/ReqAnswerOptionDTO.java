package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReqAnswerOptionDTO {

    @NotBlank(message = "Content khong duoc de trong")
    private String content;

    @NotNull(message = "Is correct khong duoc de trong")
    private Boolean isCorrect;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
