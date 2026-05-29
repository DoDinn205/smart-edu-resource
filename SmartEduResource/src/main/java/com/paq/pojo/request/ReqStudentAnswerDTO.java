/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.request;

/**
 *
 * @author Admin
 */
public class ReqStudentAnswerDTO {
    private Integer questionId;
    private Integer optionId;
    private String answerText;
    public Integer getQuestionId(){
        return questionId;
    }
    public void setQuestionId(Integer questionId){
        this.questionId=questionId;
    }
    public Integer getOptionId(){
        return optionId;
    }
    public void setOptionId(Integer optionId){
        this.optionId=optionId;
    }
    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
