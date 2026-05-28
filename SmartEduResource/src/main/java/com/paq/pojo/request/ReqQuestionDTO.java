package com.paq.pojo.request;

import com.paq.utils.constant.QuestionTypeEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReqQuestionDTO {

    @NotBlank(message = "Content khong duoc de trong")
    private String content;

    @NotNull(message = "Score khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Score phai lon hon 0")
    private Double score;

    private String explanation;

    @NotNull(message = "Type khong duoc de trong")
    private QuestionTypeEnum type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public QuestionTypeEnum getType() {
        return type;
    }

    public void setType(QuestionTypeEnum type) {
        this.type = type;
    }
}
