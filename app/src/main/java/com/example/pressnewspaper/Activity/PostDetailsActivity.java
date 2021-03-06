package com.example.pressnewspaper.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Adapter.AdapterOtherPosts;
import com.example.pressnewspaper.Adapter.SlideShow_adapter_ads;
import com.example.pressnewspaper.Model.ModelAds;
import com.example.pressnewspaper.Model.ModelOtherPosts;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ToolbarClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
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

    ViewPager viewPagerAds;
    ArrayList<ModelAds> adsArrayList1;
    SlideShow_adapter_ads adapter_ads1;
    LinearLayout lay_ads1;

    private void GetAds() {
        adsArrayList1 = new ArrayList<>();
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

        Api.RetrofitAds service = retrofit.create(Api.RetrofitAds.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("position", "3");

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");
                    switch (status_code) {
                        case "200": {
                            JSONArray data = object.getJSONArray("data");
                            for (int i = 0; i <data.length() ; i++) {
                                JSONObject item = data.getJSONObject(i);
                                ModelAds modelAds = new ModelAds();
                                modelAds.setId(item.getString("id"));
                                modelAds.setImgUrl(item.getString("image"));
                                adsArrayList1.add(modelAds);
                            }
                            if (adsArrayList1.size()>0){
                                adapter_ads1 = new SlideShow_adapter_ads(getApplicationContext(),adsArrayList1);
                                viewPagerAds.setAdapter(adapter_ads1);
                                AutoSwipingImgAds1();
                            }else {
                                lay_ads1.setVisibility(View.GONE);
                            }

                            break;
                        }
                        default: {
                            lay_ads1.setVisibility(View.GONE);
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
            }
        });
    }

    Handler handlerAds1;
    Runnable runnableAds1;
    Timer timerAds1;
    int iAds1 = 0;
    private void AutoSwipingImgAds1() {
        handlerAds1 = new Handler();
        runnableAds1 = new Runnable() {
            @Override
            public void run() {

//                int i = viewPager_slid_img.getCurrentItem();
                if (iAds1 == adsArrayList1.size() - 1) {
                    iAds1 = 0;
                } else {
                    iAds1++;
                }
                viewPagerAds.setCurrentItem(iAds1);
            }
        };
        timerAds1 = new Timer();
        timerAds1.schedule(new TimerTask() {
            @Override
            public void run() {
                handlerAds1.post(runnableAds1);
            }
        }, 3000, 6000);
    }


    LinearLayout postDataLay;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_post_details, "تفاصيل");

        token = SharedPrefManager.getInstance(getApplicationContext()).GetToken();
        initPopupMenu();
        init();
        processIsFavorite(is_favorite);

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


        GetAds();

    }

    CardView cardSorry;
    private void init() {
        lay_ads1 = findViewById(R.id.lay_ads1);
        cardSorry = findViewById(R.id.cardSorry);
        postDataLay = findViewById(R.id.postDataLay);
        viewPagerAds = findViewById(R.id.VPads1);

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


    }

    private void setRecommendedAdapter(ArrayList<ModelOtherPosts> list) {

        recyclerView.setVisibility(View.VISIBLE);
        adapterOtherPosts = new AdapterOtherPosts(this, list);
        recyclerView.setAdapter(adapterOtherPosts);

        //end of test fun
    }

    boolean is_favorite = false;
    boolean canCopy=false,canView=false;
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

                            canView = data.getBoolean("can_view");
                            canCopy = data.getBoolean("can_copy");

                            if (canView){
                                textViewTitle.setText(data.getString("title"));
                                textViewDate.setText(data.getString("published_at"));
                                textViewBody.setText(data.getString("body"));
                                textViewWriter.setText("الكاتب : " + author.getString("name"));

                                buttonNewsPaper.setText(newsPaper.getString("name"));
                                newsPaperId = newsPaper.getString("id");

                                postDataLay.setVisibility(View.VISIBLE);
                                cardSorry.setVisibility(View.GONE);
                            }else{
                                postDataLay.setVisibility(View.GONE);
                                cardSorry.setVisibility(View.VISIBLE);
                            }




                            Glide.with(PostDetailsActivity.this).load(Api.ROOT_URL + "storage/" + data.getString("image"))
                                    .into(imageView);

                            processIsFavorite(is_favorite);
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

    LinearLayout layoutMenu;
    PopupMenu popup;
    private void initPopupMenu() {
        layoutMenu = findViewById(R.id.layMenu);
        popup = new PopupMenu(PostDetailsActivity.this, layoutMenu);
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PostDetails.this.openOptionsMenu();
                //popup menu


//                popup.getMenu().getItem(R.id.item1).setTitle("");
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item1: {
                                if (canCopy){
                                    sharePost(id);}else{
                                    Toast.makeText(PostDetailsActivity.this, "عفوا لايمكنك مشاركة المقال..الرجاء الاشتراك", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case R.id.item2: {
                                if (!token.equals("")){
                                    AddFavorites();
                                }else{
                                    dialogMessage("اضافة الى المفضلة","عفوا .. \n لم تقم بتسجيل الدخول حتى الان.. نرجو تسجيل الدخول اولا للوصول لكل المميزات");
                                }
                                break;
                            }

                        }
                        return true;
                    }
                });
                popup.show();

            }
        });
    }


    private void processIsFavorite(boolean is_favorite) {
        Menu menu = popup.getMenu();
        if (is_favorite) {
            menu.getItem(1).setTitle("ازالة من المفضلة");
        } else {
            menu.getItem(1).setTitle("حفظ في المفضلة");
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
                            processIsFavorite(is_favorite);
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


    private void sharePost(String postId){
        String webUrl=Api.ROOT_URL+"posts/";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                webUrl+postId);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


}
