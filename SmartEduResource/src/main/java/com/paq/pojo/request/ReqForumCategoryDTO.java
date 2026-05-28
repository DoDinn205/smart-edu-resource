package com.paq.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ReqForumCategoryDTO {

    @NotBlank(message = "Name không được để trống")
    @Size(max = 200, message = "Name tối đa 200 ký tự")
    private String name;

    @Size(max = 65535, message = "Description tối đa 65535 ký tự")
    private String description;

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
