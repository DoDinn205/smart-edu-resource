package com.paq.pojo.response;

import java.util.List;

public class ResStudentAnswerResultDTO {

    private Integer id;
    private Integer questionId;
    private String questionContent;
    private String questionType;
    private Double questionScore;
    private String answerText;
    private Integer selectedOptionId;
    private String selectedOptionContent;
    private Boolean isCorrect;
    private Double score;
    private String explanation;
    private List<ResAnswerOptionDTO> correctOptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Double getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Double questionScore) {
        this.questionScore = questionScore;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Integer getSelectedOptionId() {
        return selectedOptionId;
    }

    public void setSelectedOptionId(Integer selectedOptionId) {
        this.selectedOptionId = selectedOptionId;
    }

    public String getSelectedOptionContent() {
        return selectedOptionContent;
    }

    public void setSelectedOptionContent(String selectedOptionContent) {
        this.selectedOptionContent = selectedOptionContent;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
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

    public List<ResAnswerOptionDTO> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(List<ResAnswerOptionDTO> correctOptions) {
        this.correctOptions = correctOptions;
    }
}
