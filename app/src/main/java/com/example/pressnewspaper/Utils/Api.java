package com.example.pressnewspaper.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class Api {
    public static String ROOT_URL = "http://onlinefit.com.sd/papers/public/";


    //Retrofit interface
    //registration
    public interface RetrofitRegister {
        @POST("api/register")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //login
    public interface RetrofitLogin {
        @POST("api/login")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //GetNewsPaper
    public interface RetrofitGetNewsPaper {
        @GET("api/newspapers")
        Call<String> putParam();
    }
    //GetSlider
    public interface RetrofitGetSlider {
        @GET("api/slider-posts")
        Call<String> putParam();
    }

    //GetPost
    public interface RetrofitGetMainPosts {
        @GET("api/posts")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }
    //GetPostDetails
    public interface RetrofitGetPostDetails {
        @GET("api/post")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }
    //GetRecommendedPost
    public interface RetrofitGetRecommendedPost {
        @GET("api/recommended-posts")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }



}
