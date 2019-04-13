package com.example.qiubo.goaltracker.view.impl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.view.IRegisterView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity implements IRegisterView, View.OnClickListener {
     Toolbar toolbar;
    ImageView backImageView;
    EditText editName,editNickName,editPassword;
    Button registerButton;
    private final String TAG="Register";
    private final String IPAdress="http://localhost/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar=(Toolbar)findViewById(R.id.login_main_toolbar);
        backImageView=findViewById(R.id.register_toolbar_imageView_back);
        editName=findViewById(R.id.register_editText_userName);
        editNickName=findViewById(R.id.register_editText_nickName);
        editPassword=findViewById(R.id.register_editText_password);
        registerButton=findViewById(R.id.register_button_register);
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
                User user=new User();
                user.setNickName(editNickName.getText().toString());
                user.setName(editName.getText().toString());
                user.setPasword(editPassword.getText().toString());

                postUser(user);
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

                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(RegisterActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "请求失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "请求成功");
                    }
                });

    }
}
