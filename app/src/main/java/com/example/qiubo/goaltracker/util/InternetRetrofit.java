package com.example.qiubo.goaltracker.util;

import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.model.ResponseUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InternetRetrofit {
    @POST("user/register")
    Observable<BaseResult> getCall(@Body User user);

    @POST("user/verification")
    Observable<BaseResult> verify(@Body ResponseUser responseUser);

    @POST("data/remote")
    Observable<BaseResult> remote(@Body List<Event> eventList);

    @POST("data/alldata")
    Observable<BaseResult> getAllEvent(@Body User user);

    @POST("user/login")
    Observable<BaseResult> login(@Body User user);

    @POST("user/login/verify")
    Observable<BaseResult> loginVerify(@Body ResponseUser responseUser);

    @POST("user/login/send/verify")
    Observable<BaseResult> loginSendVerify(@Body User user);
}
