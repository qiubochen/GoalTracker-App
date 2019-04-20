package com.example.qiubo.goaltracker.view.impl;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.presenter.ILoginPresenter;
import com.example.qiubo.goaltracker.presenter.impl.LoginPresenterImpl;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.example.qiubo.goaltracker.view.ILoginView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener{
    Toolbar toolbar;
    Button loginButton;
    TextView registerButton;
    ImageView backImageView;
    ILoginPresenter loginPresenter;
    ImageView imageViewVisable;
    EditText editTextPassWord;
    final String TOOLBARTITLE="密码登录";
    boolean showPassword=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        toolbar=(Toolbar)findViewById(R.id.login_main_toolbar);
        loginButton=findViewById(R.id.login_button_login);
        registerButton=findViewById(R.id.login_text_view_register);
        backImageView=findViewById(R.id.login_toolbar_imageView_back);
        imageViewVisable=findViewById(R.id.login_password_visable);
        editTextPassWord=findViewById(R.id.login_editText_password);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        backImageView.setOnClickListener(this);
       imageViewVisable.setOnClickListener(this);
        imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.cloaseeye48));
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
           case R.id.login_text_view_register:{
              loginPresenter.register();
           };break;
           case R.id.login_toolbar_imageView_back:{
               this.finish();
           };break;
           case R.id.login_password_visable:{
               if (showPassword){
                   imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.openeye48));
                   editTextPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                   editTextPassWord.setSelection(editTextPassWord.getText().toString().length());
                   showPassword=!showPassword;
               }else {
                   imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.cloaseeye48));
                   editTextPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                   editTextPassWord.setSelection(editTextPassWord.getText().toString().length());
                   showPassword=!showPassword;

               }
           };break;

       }
    }
}
