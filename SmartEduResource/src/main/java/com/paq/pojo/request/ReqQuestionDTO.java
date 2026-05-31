package com.paq.pojo.request;

import com.paq.utils.constant.QuestionTypeEnum;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReqQuestionDTO {

    @NotBlank(message = "Content không được để trống")
    private String content;

    @NotNull(message = "Score không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Score phải lớn hơn 0")
    private Double score;

    private String explanation;

    @NotNull(message = "Type không được để trống")
    private QuestionTypeEnum type;

    private List<ReqAnswerOptionDTO> options;

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

    public List<ReqAnswerOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<ReqAnswerOptionDTO> options) {
        this.options = options;
    }
}
