package com.paq.pojo.request;

import jakarta.validation.constraints.NotNull;

public class ReqLecturerApprovalDTO {

    @NotNull(message = "Trạng thái duyệt giảng viên là bắt buộc")
    private Boolean isApprove;

    public Boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }
}
