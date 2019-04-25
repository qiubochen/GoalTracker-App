package com.example.qiubo.goaltracker.view.impl;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.model.ResponseUser;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.example.qiubo.goaltracker.view.IRegisterView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements IRegisterView, View.OnClickListener {
     Toolbar toolbar;
    ImageView backImageView;
    EditText editName,editNickName,editPassword,editConfirmPassword;
    Button registerButton;
    ImageView imageViewVisable;
    boolean showPassword;
    private final String TAG="Register";
    private final String IPAdress="http://39.108.227.213:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        toolbar=(Toolbar)findViewById(R.id.login_main_toolbar);
        backImageView=findViewById(R.id.register_toolbar_imageView_back);
        editName=findViewById(R.id.register_editText_userName);
        editNickName=findViewById(R.id.register_editText_nickName);
        editPassword=findViewById(R.id.register_editText_password);
        editConfirmPassword=findViewById(R.id.register_editText_confirm_password);
        registerButton=findViewById(R.id.register_button_register);
        imageViewVisable=findViewById(R.id.register_image_visable);
        imageViewVisable.setOnClickListener(this);
        imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.cloaseeye48));
        backImageView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.register_toolbar_imageView_back: {
                finish();
            };break;
            case R.id.register_button_register:{
                if(editName.getText().toString().length()<=0){
                    Toast.makeText(RegisterActivity.this,"手机号还没输入",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(editNickName.getText().toString().length()<=0){
                    Toast.makeText(RegisterActivity.this,"昵称还没输入",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(editPassword.getText().toString().length()<=0){
                    Toast.makeText(RegisterActivity.this,"手机号还没输入",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(editConfirmPassword.getText().toString().length()<=0){
                    Toast.makeText(RegisterActivity.this,"手机号还没输入",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!editConfirmPassword.getText().toString().equals(editPassword.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"密码与确认密码不一致",Toast.LENGTH_SHORT).show();
                    break;

                }
                User user=new User();
                user.setNickName(editNickName.getText().toString());
                user.setUserName(editName.getText().toString());
                user.setPasword(editPassword.getText().toString());

                postUser(user);
            };break;
            case R.id.register_image_visable:{
                if (showPassword){
                    imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.openeye48));
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editPassword.setSelection(editPassword.getText().toString().length());
                    editConfirmPassword.setSelection(editConfirmPassword.getText().toString().length());
                    showPassword=!showPassword;
                }else {
                    imageViewVisable.setImageDrawable(getResources().getDrawable(R.mipmap.cloaseeye48));
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editPassword.setSelection(editPassword.getText().toString().length());
                    editConfirmPassword.setSelection(editConfirmPassword.getText().toString().length());
                    showPassword=!showPassword;

                }
            }
        }
    }
    void postUser(User user){
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPAdress) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        final InternetRetrofit request = retrofit.create(InternetRetrofit.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<BaseResult> observable = request.getCall(user);

        // 步骤7：发送网络请求
        observable.subscribeOn(Schedulers.io())               // 在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())  // 回到主线程 处理请求结果
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(BaseResult result) {
                        // 步骤8：对返回的数据进行处理
                        if (result.getCode().equals("200")){
                            String s=JSONObject.toJSONString(result);
                            JSONObject jsonObject=JSONObject.parseObject(s);
                            ResponseUser responseUser=jsonObject.getObject("object",ResponseUser.class);
                            String uuid=responseUser.getUuid();
                            Intent intent=new Intent(RegisterActivity.this,VerifyActivity.class);

                            intent.putExtra("uuid",uuid);
                            startActivity(intent);

                        }else {
                            if (result.getCode().equals("1024")){
                                Toast.makeText(RegisterActivity.this,"验证码输入次数过多",Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (result.getCode().equals("1016")){
                                Toast.makeText(RegisterActivity.this,"手机号码错误",Toast.LENGTH_SHORT).show();

                                return;
                            }
                            if (result.getCode().equals("0002")){
                                Toast.makeText(RegisterActivity.this,"用户名或昵称重复",Toast.LENGTH_SHORT).show();

                                return;
                            }
                            Toast.makeText(RegisterActivity.this,"没有权限", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "请求失败"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "请求成功");
                    }
                });

    }
}
