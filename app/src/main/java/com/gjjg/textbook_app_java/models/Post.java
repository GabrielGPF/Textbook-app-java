package com.gjjg.textbook_app_java.models;

public class Post {
    String image;
    String senderName;
    String senderId;

    public Post(String image, String senderName, String senderId) {
        this.image = image;
        this.senderName = senderName;
        this.senderId = senderId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
