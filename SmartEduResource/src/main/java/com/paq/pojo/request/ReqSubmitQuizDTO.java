/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ReqSubmitQuizDTO {
    @Valid
    @NotEmpty(message="Danh sach cau tra loi khong duoc rong")
    private List<ReqStudentAnswerDTO> answers;
    public List<ReqStudentAnswerDTO>getAnswers(){
        return answers;
    }
    public void setAnswers(List<ReqStudentAnswerDTO> answers) {
        this.answers = answers;
    }
}
