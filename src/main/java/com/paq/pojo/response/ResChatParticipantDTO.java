package com.paq.pojo.response;

import java.util.Date;

public class ResChatParticipantDTO {

    private Integer id;
    private Date joinedAt;
    private Boolean isMuted;
    private Integer roomId;
    private ResUserDTO user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Boolean getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Boolean isMuted) {
        this.isMuted = isMuted;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public ResUserDTO getUser() {
        return user;
    }

    public void setUser(ResUserDTO user) {
        this.user = user;
    }
}
