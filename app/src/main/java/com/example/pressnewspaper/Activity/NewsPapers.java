package com.example.pressnewspaper.Activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Adapter.AdapterAllNewsPaper;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.ToolbarClass;
import com.google.android.material.snackbar.Snackbar;

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


public class NewsPapers extends ToolbarClass {


    //spinner
    Spinner spinner1;
    String[] arraySpinner1;
    ArrayAdapter<String> adapter1;

    LinearLayout progressLay;
    RecyclerView recyclerView;
    AdapterAllNewsPaper adapterAllNewsPaper;
    ArrayList<ModelNewsPaper> newsPaperArrayList;
    RelativeLayout container;

    private void initSpinner() {
        //init spinner1
        spinner1 = findViewById(R.id.spinner1);
        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner1) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position
//                if (position == 0) {
//                    v.setBackgroundColor(Color.WHITE);
//                } else {
//                    if (position % 2 == 0) {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design1));
//                    } else {
//                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design2));
//                    }
//
//                }
                return v;
            }
        };
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    s_month = "";
//                } else {
//                    s_month = array_month[position];
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_news_papers, "الصحف");
        container = findViewById(R.id.container);
        progressLay = findViewById(R.id.progressLay);
//        testAdapter();
        initSpinner();


        if (isOnline()) {
            GetNewsPaper();
        } else {
            ShowSnakBar("تعذر الاتصال بالانترنت");
        }
    }

    private void GetNewsPaper() {
        newsPaperArrayList = new ArrayList<>();
        newsPaperArrayList.clear();
        progressLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetNewsPaper service = retrofit.create(Api.RetrofitGetNewsPaper.class);

//        HashMap<String, String> hashBody = new HashMap<>();
//        hashBody.put("Content-Type", "application/json;");

        Call<String> call = service.putParam();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");
                    switch (status_code) {
                        case "200": {


                            JSONArray jsonArrayData = object.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {

                                ModelNewsPaper modelNewsPaper = new ModelNewsPaper();

                                JSONObject object2 = jsonArrayData.getJSONObject(i);

                                modelNewsPaper.setNewPaperId(object2.getString("id"));
                                modelNewsPaper.setNewPaperName(object2.getString("name"));
                                modelNewsPaper.setNewPaperType(object2.getString("type_name"));
                                modelNewsPaper.setReleaseType(object2.getString("publish_period"));
                                modelNewsPaper.setReleaseTime(object2.getString("publish_day"));
                                modelNewsPaper.setImg(object2.getString("logo"));

                                newsPaperArrayList.add(modelNewsPaper);

                            }
                            if (newsPaperArrayList.size()>0){
                                initAdapter(newsPaperArrayList);
                            }else{
                                ShowSnakBar("تعذر الوصول لسجل الصحف حاول مجددا");
                            }

                            break;
                        }
                        default:{
                            ShowSnakBar("تعذر الوصول لسجل الصحف حاول مجددا");
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ShowSnakBar("تعذر الوصول لسجل الصحف حاول مجددا");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                ShowSnakBar("خطأ في الاتصال");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void initAdapter(ArrayList<ModelNewsPaper> newsPaperArrayList) {
        recyclerView = findViewById(R.id.news_paper_recycler);
        recyclerView.setNestedScrollingEnabled(false);
//        for (int i = 0; i < 10; i++) {
//            ModelNewsPaper modelNewsPaper = new ModelNewsPaper();
//            if (i % 2 == 0) {
//                modelNewsPaper.setNewPaperName("صحيفة الصحافة");
//                modelNewsPaper.setNewPaperType("سياسية");
//                modelNewsPaper.setReleaseType("اسبوعية");
//                modelNewsPaper.setReleaseTime("تصدر يوم الاحد");
//            } else {
//                modelNewsPaper.setNewPaperName("صحيفة الدار");
//                modelNewsPaper.setNewPaperType("اخبار");
//                modelNewsPaper.setReleaseType("يومية");
//                modelNewsPaper.setReleaseTime("تصدر الساعة 8 صباحة");
//            }
//            newsPaperArrayList.add(modelNewsPaper);
            adapterAllNewsPaper = new AdapterAllNewsPaper(this, newsPaperArrayList);
            recyclerView.setAdapter(adapterAllNewsPaper);
        }

        //end of test fun


    private void ShowSnakBar(String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.GRAY);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(14);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
