package com.example.qiubo.goaltracker.util;

import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InternetRetrofit {
    @POST("user/register")
    Observable<BaseResult> getCall(@Body User user);
}
