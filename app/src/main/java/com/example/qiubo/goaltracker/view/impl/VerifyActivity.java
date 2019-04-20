package com.example.qiubo.goaltracker.view.impl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.example.qiubo.goaltracker.R;

public class VerifyActivity extends AppCompatActivity {
    VerificationCodeInput verificationCodeInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        verificationCodeInput=findViewById(R.id.register_verificationCodeInput);
        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String s) {
                Toast.makeText(VerifyActivity.this,s,Toast.LENGTH_SHORT).show();
                for (int i=0;i<verificationCodeInput.getChildCount();i++){//遍历子类
                    verificationCodeInput.getChildAt(i).setEnabled(true);//设置可点击
                    verificationCodeInput.getChildAt(i).setClickable(true);
                    EditText childAt = (EditText) verificationCodeInput.getChildAt(i);
                    childAt.setText("");//清空内容
                    if(i==0){//第一个获取焦点
                        verificationCodeInput.getChildAt(i).requestFocus();
                        verificationCodeInput.getChildAt(i).setFocusable(true);
                        verificationCodeInput.getChildAt(i).setFocusableInTouchMode(true);
                    }
                }
                verificationCodeInput.setClickable(true);
                verificationCodeInput.setEnabled(true);
            }
        });
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VerifyActivity.this,"sad",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
