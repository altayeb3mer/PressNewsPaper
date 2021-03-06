package com.example.pressnewspaper.Utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class Api {
    public static String ROOT_URL = "https://shamilpress.com/";


    //Retrofit interface
    //registration
    public interface RetrofitRegister {
        @POST("api/register")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //sendOtp
    public interface RetrofitSendOtp {
        @POST("api/send-otp")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //verifyOtp
    public interface RetrofitVerifyOtp {
        @POST("api/check-otp")
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
    //GetNewsPaperDetails
    public interface RetrofitGetNewsPaperDetails {
        @GET("api/newspapers")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }
    //newspapers
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
    //GetUrgentPost
    public interface RetrofitGetUrgentPost {
        @GET("api/urgent-posts")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }
    //MySub
    public interface RetrofitGetMySub {
        @GET("api/my-subscriptions")
        Call<String> putParam();
    }
    //MySaved
    public interface RetrofitGetMySaved {
        @GET("api/favorites")
        Call<String> putParam();
    }
    //AllSub
    public interface RetrofitGetAllSub {
        @GET("api/subscriptions")
        Call<String> putParam();
    }
    //subscribe
    public interface RetrofitSubscribe {
        @POST("api/subscribe")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //AddFavorites
    public interface RetrofitAddFavorites {
        @POST("api/favorites")
        Call<String> putParam(@Body HashMap<String, String> param);
    }
    //search
    public interface RetrofitSearch {
        @GET("api/search")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }
    //ads
    public interface RetrofitAds {
        @GET("api/ads")
        Call<String> putParam(@QueryMap HashMap<String, String> param);
    }



}
