package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReqSubjectDTO {

    @Size(max = 50, message = "Code toi da 50 ky tu")
    private String code;

    @NotBlank(message = "Name khong duoc de trong")
    @Size(max = 200, message = "Name toi da 200 ky tu")
    private String name;

    @Size(max = 65535, message = "Description toi da 65535 ky tu")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
