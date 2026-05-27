package com.paq.pojo.request;

import jakarta.validation.constraints.NotNull;

public class ReqUserStatusDTO {

    @NotNull(message = "Trạng thái tài khoản là bắt buộc")
    private Boolean isActive;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
