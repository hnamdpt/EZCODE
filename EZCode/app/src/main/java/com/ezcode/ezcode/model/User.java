package com.ezcode.ezcode.model;

public class User {
    private String displayName;
    private String email;
    private String avatarUrl;
    private int point;

    public User() {

    }

    public User(String displayName, String email, String avatarUrl, int point) {
        this.displayName = displayName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.point = point;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}

