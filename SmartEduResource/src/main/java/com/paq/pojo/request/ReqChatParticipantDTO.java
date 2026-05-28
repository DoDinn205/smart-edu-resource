package com.paq.pojo.request;

import jakarta.validation.constraints.NotNull;

public class ReqChatParticipantDTO {

    @NotNull(message = "User id khong duoc de trong")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
