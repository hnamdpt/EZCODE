package com.ezcode.ezcode.model;

public class Chat {
    private String userAvatar;
    private String userName;
    private String chatContent;
    private String userEmail;
    public Chat() {

    }

    public Chat(String userAvatar, String userName, String chatContent, String userEmail) {
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.chatContent = chatContent;
        this.userEmail = userEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
