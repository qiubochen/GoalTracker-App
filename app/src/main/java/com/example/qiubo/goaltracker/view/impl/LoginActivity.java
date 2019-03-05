package com.example.qiubo.goaltracker.view.impl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.presenter.ILoginPresenter;
import com.example.qiubo.goaltracker.presenter.impl.LoginPresenterImpl;
import com.example.qiubo.goaltracker.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener{
    Toolbar toolbar;
    Button loginButton;
    Button registerButton;
    ImageView backImageView;
    ILoginPresenter loginPresenter;
    final String TOOLBARTITLE="密码登录";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar=(Toolbar)findViewById(R.id.login_main_toolbar);
        loginButton=findViewById(R.id.login_button_login);
        registerButton=findViewById(R.id.login_button_register);
        backImageView=findViewById(R.id.login_toolbar_imageView_back);


        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        backImageView.setOnClickListener(this);


        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        loginPresenter=new LoginPresenterImpl(this);
    }

    @Override
    public void onClick(View view) {
       int id=view.getId();
       switch (id){
           case R.id.login_button_login:{

           };break;
           case R.id.login_button_register:{
              loginPresenter.register();
           };break;
           case R.id.login_toolbar_imageView_back:{
               this.finish();
           };break;

       }
    }
}
