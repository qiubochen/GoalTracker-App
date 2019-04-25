package com.example.qiubo.goaltracker.view.impl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dalimao.corelibrary.VerificationCodeInput;
import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.model.ResponseUser;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyActivity extends AppCompatActivity {
    VerificationCodeInput verificationCodeInput;
    ConstraintLayout constraintLayout;
    Intent intent;
    String uuid;
    private final String IPAdress="http://39.108.227.213:8080/";
    private final String TAG="VerifyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        setContentView(R.layout.activity_verify);
        intent=getIntent();
        uuid=intent.getStringExtra("uuid");
        verificationCodeInput=findViewById(R.id.register_verificationCodeInput);
        constraintLayout=findViewById(R.id.verfiy_layout);
        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String s) {
                ResponseUser responseUser=new ResponseUser();
                responseUser.setVerifyCode(s);
                responseUser.setUuid(uuid);
                postResponseUser(responseUser);
            }
        });


    }

    void postResponseUser(ResponseUser responseUser){
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPAdress) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        final InternetRetrofit request = retrofit.create(InternetRetrofit.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<BaseResult> observable = request.verify(responseUser);

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

                        if (result.getCode().equals("0004")){
                            Toast.makeText(VerifyActivity.this,"输入验证码错误",Toast.LENGTH_SHORT).show();
                            ViewGroup verfiy=findViewById(R.id.verfiy_layout);
                            verfiy.invalidate();
                            return;
                        }
                        if (result.getCode().equals("0005")){
                            Toast.makeText(VerifyActivity.this,"验证码已过期",Toast.LENGTH_SHORT).show();
                            ViewGroup verfiy=findViewById(R.id.verfiy_layout);
                            verfiy.invalidate();
                            return;
                        }
                        if (result.getCode().equals("200")){
                            String s=JSONObject.toJSONString(result);
                            JSONObject jsonObject=JSONObject.parseObject(s);
                            User userTemp=jsonObject.getObject("object",User.class);
                            String nickS=userTemp.getNickName();
                            SharedPreUtils sharedPreUtils=new SharedPreUtils(VerifyActivity.this);
                            sharedPreUtils.put("nickName",nickS);
                            sharedPreUtils.put("userId",userTemp.getId().toString());
                            DateUtil.changeUserId(userTemp.getId().toString());
                            DateUtil.deleteOtherUserEvent(userTemp.getId().toString());
                           Intent intent=new Intent(VerifyActivity.this,MainActivity.class);


                            startActivity(intent);
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
