/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.response;

/**
 *
 * @author Admin
 */
import com.fasterxml.jackson.annotation.JsonInclude;

public class ResAnswerOptionDTO {

    private Integer id;
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isCorrect;

    public ResAnswerOptionDTO() {
    }

    public ResAnswerOptionDTO(Integer id, String content, Boolean isCorrect) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
    }

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

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
