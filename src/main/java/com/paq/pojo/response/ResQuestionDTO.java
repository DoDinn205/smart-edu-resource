package com.paq.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class ResQuestionDTO {

    private Integer id;
    private String content;
    private Double score;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String explanation;
    private String type;
    private Integer quizId;
    private List<ResAnswerOptionDTO> answers;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public List<ResAnswerOptionDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ResAnswerOptionDTO> answers) {
        this.answers = answers;
    }
}
