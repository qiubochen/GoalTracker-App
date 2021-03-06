package com.example.qiubo.goaltracker.view.impl;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.model.ResponseUser;
import com.example.qiubo.goaltracker.presenter.ILoginPresenter;
import com.example.qiubo.goaltracker.presenter.impl.LoginPresenterImpl;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.example.qiubo.goaltracker.view.ILoginView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.w3c.dom.Text;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener{
    Toolbar toolbar;
    Button loginButton;
    TextView registerButton,loginVerifyTextView;
    ImageView backImageView;
    ILoginPresenter loginPresenter;
    ImageView imageViewVisable;
    EditText editTextPassWord,editTextUserName;
    final String TOOLBARTITLE="密码登录";
    boolean showPassword=true;
    String TAG="LoginActivity";
    private final String IPAdress="http://39.108.227.213:8080/";
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
        editTextUserName=findViewById(R.id.login_editText_userName);
        loginVerifyTextView=findViewById(R.id.login_text_view_login_verify);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        backImageView.setOnClickListener(this);
       imageViewVisable.setOnClickListener(this);
       loginVerifyTextView.setOnClickListener(this);
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
               User user=new User();
               user.setUserName(editTextUserName.getText().toString());
               user.setPasword(editTextPassWord.getText().toString());
               postLogin(user);
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
           case R.id.login_text_view_login_verify:{
               Intent intent=new Intent(LoginActivity.this,LoginVerifyActivity.class);
               startActivity(intent);
           }

       }
    }
    void postLogin(User user){
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPAdress) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        final InternetRetrofit request = retrofit.create(InternetRetrofit.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        Observable<BaseResult> observable = request.login(user);

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
                            User userTemp=jsonObject.getObject("object",User.class);
                            String nickS=userTemp.getNickName();
                            SharedPreUtils sharedPreUtils=new SharedPreUtils(LoginActivity.this);
                            sharedPreUtils.put("nickName",nickS);
                            sharedPreUtils.put("userId",userTemp.getId().toString());
                            DateUtil.changeUserId(userTemp.getId().toString());
                            DateUtil.deleteOtherUserEvent(userTemp.getId().toString());
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                        }
                            if (result.getCode().equals("0001")){
                                Toast.makeText(LoginActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();

                                return;
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
