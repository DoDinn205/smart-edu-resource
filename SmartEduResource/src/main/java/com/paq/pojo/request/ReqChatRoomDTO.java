package com.paq.pojo.request;

import com.paq.utils.constant.ChatRoomTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReqChatRoomDTO {

    @NotNull(message = "Type không được để trống")
    private ChatRoomTypeEnum type;

    @Size(max = 255, message = "Name tối đa 255 ký tự")
    private String name;

    private Integer courseId;

    public ChatRoomTypeEnum getType() {
        return type;
    }

    public void setType(ChatRoomTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
