package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReqCategoryDTO {

    @NotBlank(message = "Name khong duoc de trong")
    @Size(max = 100, message = "Name toi da 100 ky tu")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
