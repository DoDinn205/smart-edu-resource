package com.paq.pojo.response;

public class ResLoginDTO {

    private String accessToken;
    private Integer userId;
    private String username;
    private String role;

    public ResLoginDTO() {
    }

    public ResLoginDTO(String accessToken, Integer userId, String username, String role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
