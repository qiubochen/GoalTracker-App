package com.example.qiubo.goaltracker.presenter.impl;

import android.content.Context;
import android.content.Intent;

import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.presenter.ILoginPresenter;
import com.example.qiubo.goaltracker.view.ILoginView;
import com.example.qiubo.goaltracker.view.impl.LoginActivity;
import com.example.qiubo.goaltracker.view.impl.RegisterActivity;

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView iLoginView;

       public LoginPresenterImpl(ILoginView iLoginView) {
        this.iLoginView=iLoginView;
    }

    @Override
    public boolean isComplet(User user) {
        return false;
    }

    @Override
    public boolean isBlank() {
        return false;
    }

    @Override
    public void login() {
        Intent intent=new Intent();
        intent.setClass((Context) iLoginView,RegisterActivity.class);
        ((Context) iLoginView).startActivity(intent);

    }

    @Override
    public void register() {
        Intent intent=new Intent((Context) iLoginView,RegisterActivity.class);
        ((Context) iLoginView).startActivity(intent);
    }


}
