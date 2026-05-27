/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.response;

import java.util.List;

/**
 *
 * @author Admin
 */
public class ResQuestionDTO {
    private Integer id;
    private String content;
    private Double score;
    private String type;
    private List<ResAnswerOptionDTO> options;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ResAnswerOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<ResAnswerOptionDTO> options) {
        this.options = options;
    }
}
