package com.paq.pojo.request;

import com.paq.utils.constant.EnrollmentStatusEnum;

import jakarta.validation.constraints.NotNull;

public class ReqEnrollmentStatusDTO {

    @NotNull(message = "Status không được để trống")
    private EnrollmentStatusEnum status;

    public EnrollmentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatusEnum status) {
        this.status = status;
    }
}
