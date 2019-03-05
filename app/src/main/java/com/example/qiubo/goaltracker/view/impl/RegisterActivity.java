package com.example.qiubo.goaltracker.view.impl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.view.IRegisterView;

public class RegisterActivity extends AppCompatActivity implements IRegisterView, View.OnClickListener {
     Toolbar toolbar;
    ImageView backImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar=(Toolbar)findViewById(R.id.login_main_toolbar);
        backImageView=findViewById(R.id.register_toolbar_imageView_back);
        backImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.register_toolbar_imageView_back: {
                finish();
            };break;
        }
    }
}
