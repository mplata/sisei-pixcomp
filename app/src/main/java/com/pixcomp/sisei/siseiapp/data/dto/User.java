package com.pixcomp.sisei.siseiapp.data.dto;

/**
 * Created by Marcos Plata on 24/10/2017.
 */

public class User {

    private String firebaseUid;
    private String email;
    private String nickname;

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
