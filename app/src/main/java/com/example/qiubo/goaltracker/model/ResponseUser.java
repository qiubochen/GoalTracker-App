package com.example.qiubo.goaltracker.model;

import com.example.qiubo.goaltracker.model.DO.User;
public class ResponseUser extends User {
    String uuid;
    String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
