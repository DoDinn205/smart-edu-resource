/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author Admin
 */
public class ReqChatMessageDTO {

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
