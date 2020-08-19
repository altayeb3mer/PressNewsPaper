package com.example.pressnewspaper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Adapter.AdapterOtherPosts;
import com.example.pressnewspaper.Model.ModelOtherPosts;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ToolbarClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PostDetailsActivity extends ToolbarClass {

    LinearLayout progressLayRec;

    RelativeLayout container;

    RecyclerView recyclerView;
    AdapterOtherPosts adapterOtherPosts;
    ArrayList<ModelOtherPosts> otherPostsArrayList;

    ImageView imageView;
    LinearLayout progressLay;
    AppCompatButton buttonNewsPaper;
    TextView textViewTitle, textViewWriter, textViewDate, textViewBody;

    String id = "", newsPaperId = "";

    ImageView addFavorites;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_post_details, "تفاصيل");

        token = SharedPrefManager.getInstance(getApplicationContext()).GetToken();
        init();
        processIsFavorate(is_favorite);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            id = args.getString("id");
            GetPostsDetails();
            GetRecommendedPost();
        }


        buttonNewsPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewsPaperDetails.class);
                intent.putExtra("id",newsPaperId);
                startActivity(intent);
            }
        });


    }

    private void init() {
        addFavorites = findViewById(R.id.addFavorites);
        recyclerView = findViewById(R.id.post_details_recycler);
        progressLayRec = findViewById(R.id.progressLayRec);
        container = findViewById(R.id.container);
        imageView = findViewById(R.id.img);
        progressLay = findViewById(R.id.progressLay);
        buttonNewsPaper = findViewById(R.id.btn);
        textViewTitle = findViewById(R.id.title);
        textViewWriter = findViewById(R.id.writer);
        textViewDate = findViewById(R.id.date);
        textViewBody = findViewById(R.id.body);

        addFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!token.equals("")){
                    AddFavorites();
                }else{
                    dialogMessage("اضافة الى المفضلة","عفوا .. \n لم تقم بتسجيل الدخول حتى الان.. نرجو تسجيل الدخول اولا للوصول لكل المميزات");
                }
            }
        });

    }

    private void setRecommendedAdapter(ArrayList<ModelOtherPosts> list) {

        recyclerView.setVisibility(View.VISIBLE);
        adapterOtherPosts = new AdapterOtherPosts(this, list);
        recyclerView.setAdapter(adapterOtherPosts);

        //end of test fun
    }

    boolean is_favorite = false;
    private void GetPostsDetails() {
        container.setVisibility(View.GONE);
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
//                        ongoing.addHeader("Content-Type", "application/json;");
//                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

                        ongoing.addHeader("Authorization",token);

                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);

        Api.RetrofitGetPostDetails service = retrofit.create(Api.RetrofitGetPostDetails.class);

//        HashMap<String, String> hashBody = new HashMap<>();
//        hashBody.put("Content-Type", "application/json;");

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");


                    switch (status_code) {
                        case "200": {
                            JSONObject data = object.getJSONObject("data");
                            JSONObject newsPaper = data.getJSONObject("newspaper");
                            JSONObject author = data.getJSONObject("author");
                            is_favorite = data.getBoolean("is_favorite");


                            textViewTitle.setText(data.getString("title"));
                            textViewDate.setText(data.getString("published_at"));
                            textViewBody.setText(data.getString("body"));
                            textViewWriter.setText("الكاتب : " + author.getString("name"));

                            buttonNewsPaper.setText(newsPaper.getString("name"));
                            newsPaperId = newsPaper.getString("id");

                            Glide.with(PostDetailsActivity.this).load(Api.ROOT_URL + "storage/" + data.getString("image"))
                                    .into(imageView);

                            processIsFavorate(is_favorite);
                            container.setVisibility(View.VISIBLE);
                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void processIsFavorate(boolean is_favorite) {
        try {
            if (is_favorite){
                addFavorites.setImageResource(R.drawable.ic_bookmark);
            }else{
                addFavorites.setImageResource(R.drawable.ic_saved);
            }
        }catch (Exception e){
            addFavorites.setImageResource(R.drawable.ic_saved);
        }

    }


    private void GetRecommendedPost() {
        otherPostsArrayList = new ArrayList<>();
        progressLayRec.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
//                        ongoing.addHeader("Content-Type", "application/json;");
//                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);

        Api.RetrofitGetRecommendedPost service = retrofit.create(Api.RetrofitGetRecommendedPost.class);

//        HashMap<String, String> hashBody = new HashMap<>();
//        hashBody.put("Content-Type", "application/json;");

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");


                    switch (status_code) {
                        case "200": {
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);

                                ModelOtherPosts otherPosts = new ModelOtherPosts();
                                otherPosts.setPostId(item.getString("id"));
                                otherPosts.setTitle(item.getString("title"));
                                otherPosts.setImg_url(item.getString("image"));

                                //sub obj
                                JSONObject newsPaperObj = item.getJSONObject("newspaper");
                                otherPosts.setNewsPaperName(newsPaperObj.getString("name"));


                                otherPostsArrayList.add(otherPosts);
                            }

                            if (otherPostsArrayList.size() > 0) {
                                setRecommendedAdapter(otherPostsArrayList);
                            } else {
                                Toast.makeText(getApplicationContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLayRec.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
                progressLayRec.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLayRec.setVisibility(View.GONE);
            }
        });
    }

    String token="";
    private void AddFavorites() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
//                        ongoing.addHeader("Content-Type", "application/json;");
//                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

                        ongoing.addHeader("Authorization",token);

                        return chain.proceed(ongoing.build());
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.ROOT_URL)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api.RetrofitAddFavorites service = retrofit.create(Api.RetrofitAddFavorites.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("id", id);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");
                    switch (status_code) {
                        case "200": {
                            JSONObject data = object.getJSONObject("data");
                            String msg = data.getString("message");
                            dialogMessage("اضافة الى المفضلة",msg);
                            if (is_favorite){
                                is_favorite = false;
                            }else{
                                is_favorite = true;
                            }
                            processIsFavorate(is_favorite);
                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "تعذر الوصول لسجل للخادم حاول مجددا", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void dialogMessage(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(PostDetailsActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "تم",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
