package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
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

import static android.content.ContentValues.TAG;

public class DeptPostsActivity extends ToolbarClass {

    Spinner spinner1;
    LinearLayout spinner1Lay;
    NestedScrollView nestedScrollView;
    LinearLayout progressLay;

    ArrayList<String> listSpinner;
    ArrayList<ModelNewsPaper> newsPaperArrayList;
    ArrayAdapter<String> adapter1;

    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;

    String s_current_page = "", s_last_page = "", s_perPage = "", s_newsPaperId = "", s_category = "";

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        s_category = args.getString("category");
        super.onCreate(R.layout.activity_deopt_posts, s_category);
        init();
        listSpinner = new ArrayList<>();
        GetNewsPaper();
        postsCardArrayList = new ArrayList<>();
        GetPosts(s_newsPaperId, s_category, s_current_page);

    }

    private void init() {
        spinner1 = findViewById(R.id.spinner1);
        spinner1Lay = findViewById(R.id.spinner1Lay);
        recyclerViewPosts = findViewById(R.id.recycler);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        nestedScrollView =findViewById(R.id.nestedScroll);
        progressLay = findViewById(R.id.progressLay);
        spinner1Lay.setVisibility(View.INVISIBLE);

    }

    private void GetPosts(String newsPaperId, String category, String currentPage) {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
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
        hashMap.put("newspaper", newsPaperId);
        hashMap.put("category", category);
        hashMap.put("page", currentPage);

        Api.RetrofitGetMainPosts service = retrofit.create(Api.RetrofitGetMainPosts.class);

//        HashMap<String, String> hashBody = new HashMap<>();
//        hashBody.put("Content-Type", "application/json;");

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");

                    JSONObject object2 = object.getJSONObject("data");
                    switch (status_code) {
                        case "200": {
                            s_current_page = object2.getString("current_page");
                            s_last_page = object2.getString("last_page");
                            s_perPage = object2.getString("per_page");

                            JSONArray jsonArrayData = object2.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JSONObject item = jsonArrayData.getJSONObject(i);

                                ModelPostsCard modelPostsCard = new ModelPostsCard();
                                modelPostsCard.setId(item.getString("id"));
                                modelPostsCard.setTitle(item.getString("title"));
                                modelPostsCard.setDate(item.getString("published_at"));
                                modelPostsCard.setCategory(item.getString("category"));
                                modelPostsCard.setNewsPaperId(item.getString("newspaper_id"));
                                modelPostsCard.setImg_url(item.getString("image"));

                                //sub obj
                                JSONObject newsPaperItem = item.getJSONObject("newspaper");
                                modelPostsCard.setNewsPaperName(newsPaperItem.getString("name"));

                                postsCardArrayList.add(modelPostsCard);

                            }
                            if (postsCardArrayList.size() > 0) {
                                initPostAdapter(postsCardArrayList);
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
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    GridLayoutManager gridLayoutManager;
    private void initPostAdapter(final ArrayList<ModelPostsCard> list) {
        recyclerViewPosts.setLayoutManager(gridLayoutManager);
        recyclerViewPosts.setNestedScrollingEnabled(false);

        if (list.size() > 0) {
            adapterPostsCard = new AdapterPostsCard(this, list);
            recyclerViewPosts.setAdapter(adapterPostsCard);

        } else {
            Toast.makeText(this, "لاتوجد منشورات حاول مجددا", Toast.LENGTH_SHORT).show();
        }

        if (Integer.parseInt(s_last_page) > Integer.parseInt(s_current_page)) {
            recyclerViewPosts.scrollTo(list.size() - Integer.parseInt(s_perPage), list.size());
            recyclerViewPosts.scrollToPosition(list.size() - Integer.parseInt(s_perPage));
            adapterPostsCard.notifyItemInserted(list.size() - Integer.parseInt(s_perPage));
        }




        //get last view on nestedScrollView
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i(TAG, "BOTTOM SCROLL");
                    if (Double.parseDouble(s_last_page) > Double.parseDouble(s_current_page))
                        GetPosts(s_newsPaperId, s_category, Integer.parseInt(s_current_page) + 1 + "");

                }
            }
        });


    }




    private void initSpinnerPapers() {
        //init spinner1
        listSpinner.add("الصحف");
        listSpinner.add("كل الصحف");
        for (int i = 0; i < newsPaperArrayList.size(); i++) {
            listSpinner.add(newsPaperArrayList.get(i).getNewPaperName());
        }
        spinner1Lay.setVisibility(View.VISIBLE);
        spinner1 = findViewById(R.id.spinner1);
//        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, listSpinner) {
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
                if (position == 0) {

                } else if (position == 1) {
                    s_newsPaperId = "";
                    postsCardArrayList.clear();
                    if (adapterPostsCard != null)
                        adapterPostsCard.notifyDataSetChanged();
                    GetPosts(s_newsPaperId, s_category, "");
                } else {
                    s_newsPaperId = newsPaperArrayList.get(position - 2).getNewPaperId();
                    postsCardArrayList.clear();
                    if (adapterPostsCard != null)
                        adapterPostsCard.notifyDataSetChanged();
                    GetPosts(s_newsPaperId, s_category, "");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void GetNewsPaper() {
        newsPaperArrayList = new ArrayList<>();
        newsPaperArrayList.clear();
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

                            if (newsPaperArrayList.size() > 0) {
                                if (listSpinner.isEmpty())
                                    initSpinnerPapers();
                            } else {
                                Toast.makeText(getApplicationContext(), "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        default: {

                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
//                Toast.makeText(getApplicationContext(), "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();

            }
        });
    }


}