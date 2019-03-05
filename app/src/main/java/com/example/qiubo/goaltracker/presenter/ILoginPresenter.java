package com.example.qiubo.goaltracker.presenter;

import android.support.v7.app.AppCompatActivity;

import com.example.qiubo.goaltracker.model.DO.User;

public interface ILoginPresenter {
    boolean isComplet(User user);
    boolean isBlank();
    void login();
    void register();
}
