package com.pixcomp.sisei.siseiapp.data.dto;

/**
 * Created by Marcos Plata on 24/10/2017.
 */

public class Message {

    private float date;
    private String message;
    private String userUid;
    private String userNickname;

    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
