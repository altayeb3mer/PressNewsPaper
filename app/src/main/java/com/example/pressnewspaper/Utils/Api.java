package com.example.pressnewspaper.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class Api {
    public static String ROOT_URL = "http://onlinefit.com.sd/papers/public/";



    //Retrofit interface
    public interface RetrofitRegister {
        @POST("api/register")
        Call<String> putParam(@Body HashMap<String, String> phone);
    }



}