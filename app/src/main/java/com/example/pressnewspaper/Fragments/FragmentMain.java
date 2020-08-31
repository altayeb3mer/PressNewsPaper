package com.example.pressnewspaper.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Adapter.SlideShow_adapter_ads;
import com.example.pressnewspaper.Adapter.SlideShow_adapter_main;
import com.example.pressnewspaper.Model.ModelAds;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.ContentValues.TAG;


public class FragmentMain extends Fragment {
    GridLayoutManager gridLayoutManager;
    RelativeLayout loadingLay;
    ViewPager viewPager_slid_img,viewPagerAds1, viewPagerAds2;
    SlideShow_adapter_main slideShow_adapter_main;
    ArrayList<ModelSliderImg> modelSliderImgArrayList;

    ArrayList<ModelAds> adsArrayList1, adsArrayList2;


    RelativeLayout noInternetContainer;

    //spinner
    Spinner spinner1;
    String[] arraySpinner2data = new String[]{"التصنيف", "الكل", "أخبار", "أعمدة ومقالات", "تقارير وتحقيقات", "حوارات", "ثقافة ومنوعات"}, arraySpinner2;
    ArrayAdapter<String> adapter1, adapter2;
    //    AppCompatButton buttonShowMore;
    View view;
    LinearLayout progressLay, progressLaySlider;
    CircleIndicator indicator;
    Handler handler;
    Runnable runnable;
    Timer timer;
    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    String s_current_page = "1", s_last_page = "1", s_perPage = "", s_newsPaperId = "", s_category = "";
    ArrayList<ModelNewsPaper> newsPaperArrayList;
    LinearLayout spinner1Lay;

    AppCompatButton buttonRetry;
    ArrayList<String> list;
    Context mContext;
    NestedScrollView nestedScrollView;


    SwipeRefreshLayout swipeRefresh;
    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        list = new ArrayList<>();
        init();
//        initSpinner();
        GetSlider();
        GetPosts(s_newsPaperId, s_category, s_current_page);
        if (list.isEmpty())
            GetNewsPaper();

        GetAds1();
        GetAds2();
        return view;
    }

//    private void autoSwipeAds() {
//        if (adsArrayList1.size()>1){
//            AutoSwipingImgAds1();
//        }
//        if (adsArrayList2.size()>1){
//            AutoSwipingImgAds2();
//        }
//    }

    //end of test fun

    RelativeLayout hallLay;

    private void initSpinnerPapers() {
        //init spinner1
        list.add("الصحف");
        list.add("كل الصحف");
        for (int i = 0; i < newsPaperArrayList.size(); i++) {
            list.add(newsPaperArrayList.get(i).getNewPaperName());
        }
        spinner1Lay.setVisibility(View.VISIBLE);
        spinner1 = view.findViewById(R.id.spinner1);
//        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, list) {
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

//    private void initSpinner() {
//
//        //init spinner2
////        spinner2 = view.findViewById(R.id.spinner2);
//        arraySpinner2 = new String[]{"التصنيف", "الكل", "أخبار", "أعمدة", "تقارير", "حوارات", "منوعات"};
//        adapter2 = new ArrayAdapter<String>(mContext, R.layout.spinner_item, arraySpinner2) {
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                View v = null;
//                v = super.getDropDownView(position, null, parent);
//                // If this is the selected item position
////                if (position == 0) {
////                    v.setBackgroundColor(Color.WHITE);
////                } else {
////                    if (position % 2 == 0) {
////                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design1));
////                    } else {
////                        v.setBackgroundColor(getResources().getColor(R.color.spinner_bg_design2));
////                    }
////
////                }
//                return v;
//            }
//        };
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner2.setAdapter(adapter2);
//        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//
//                } else if (position == 1) {
//                    s_category = "";
//                    postsCardArrayList.clear();
//                    if (adapterPostsCard != null)
//                        adapterPostsCard.notifyDataSetChanged();
//                    GetPosts(s_newsPaperId, s_category, "");
//                } else {
//                    s_category = arraySpinner2data[position];
//                    postsCardArrayList.clear();
//                    if (adapterPostsCard != null)
//                        adapterPostsCard.notifyDataSetChanged();
//                    GetPosts(s_newsPaperId, s_category, "");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }


    @Override
    public void onResume() {
        super.onResume();

        hallLay.requestFocus();

//        viewPager_slid_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrollStateChanged(int state) {}
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//
//            public void onPageSelected(int position) {
//                // Check if this is the page you want.
//
////                timer.cancel();
//
//            }
//        });
    }

    SlideShow_adapter_ads adapter_ads1,adapter_ads2;
//    ImageView imgAds1, imgAds2;
    private void init() {
        hallLay = view.findViewById(R.id.hallLay);
        swipeRefresh= view.findViewById(R.id.swipeRefresh);

        viewPagerAds1 = view.findViewById(R.id.VPads1);
        viewPagerAds2 = view.findViewById(R.id.VPads2);

//        imgAds1 = view.findViewById(R.id.imgAds1);
//        imgAds2 = view.findViewById(R.id.imgAds2);
        nestedScrollView = view.findViewById(R.id.nestedScroll);
        gridLayoutManager = new GridLayoutManager(mContext, 1);
        loadingLay = view.findViewById(R.id.loadingLay);
        buttonRetry = view.findViewById(R.id.btnRetry);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetContainer.setVisibility(View.GONE);
                GetSlider();
                GetPosts(s_newsPaperId, s_category, s_current_page);
                if (list.isEmpty())
                    GetNewsPaper();

            }
        });
        noInternetContainer = view.findViewById(R.id.noInternetContainer);
        noInternetContainer.setVisibility(View.GONE);
        spinner1Lay = view.findViewById(R.id.spinner1Lay);
        spinner1Lay.setVisibility(View.GONE);
        postsCardArrayList = new ArrayList<>();
        progressLay = view.findViewById(R.id.progressLay);
        progressLaySlider = view.findViewById(R.id.progressLaySlider);

        viewPager_slid_img = view.findViewById(R.id.viewpager_slid_img);
        modelSliderImgArrayList = new ArrayList<>();
        indicator = view.findViewById(R.id.indicator);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_current_page="1";
                swipeRefresh.setRefreshing(true);
                postsCardArrayList = new ArrayList<>();
                modelSliderImgArrayList = new ArrayList<>();
                postsCardArrayList.clear();
                modelSliderImgArrayList.clear();
                if (adapterPostsCard!=null){
                    adapterPostsCard=new AdapterPostsCard(getActivity(),postsCardArrayList);
                    recyclerViewPosts.setAdapter(adapterPostsCard);
                }
                if (slideShow_adapter_main!=null){
                    slideShow_adapter_main = new SlideShow_adapter_main(mContext,modelSliderImgArrayList);
                    viewPager_slid_img.setAdapter(slideShow_adapter_main);
                }
                noInternetContainer.setVisibility(View.GONE);
                GetSlider();
                GetPosts(s_newsPaperId, s_category, s_current_page);
                if (list.isEmpty())
                    GetNewsPaper();
                GetAds1();
                GetAds2();
            }
        });


    }

    int i=0;
    private void AutoSwipingImg() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

//                int i = viewPager_slid_img.getCurrentItem();
                if (i == modelSliderImgArrayList.size() - 1) {
                    i = 0;
                } else {
                    i++;
                }
                viewPager_slid_img.setCurrentItem(i);


            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 6000);
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
                viewPagerAds1.setCurrentItem(iAds1);
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

    Handler handlerAds2;
    Runnable runnableAds2;
    Timer timerAds2;
    int iAds2 = 0;
    private void AutoSwipingImgAds2() {
        handlerAds2 = new Handler();
        runnableAds2 = new Runnable() {
            @Override
            public void run() {

//                int i = viewPager_slid_img.getCurrentItem();
                if (iAds2 == adsArrayList2.size() - 1) {
                    iAds2 = 0;
                } else {
                    iAds2++;
                }
                viewPagerAds2.setCurrentItem(iAds2);
            }
        };
        timerAds2 = new Timer();
        timerAds2.schedule(new TimerTask() {
            @Override
            public void run() {
                handlerAds2.post(runnableAds2);
            }
        }, 3000, 6000);
    }

    private void initPostAdapter(final ArrayList<ModelPostsCard> list) {
        recyclerViewPosts = view.findViewById(R.id.fragment_main_recycler);
        recyclerViewPosts.setLayoutManager(gridLayoutManager);

        recyclerViewPosts.setNestedScrollingEnabled(false);

        if (list.size() > 0) {
            adapterPostsCard = new AdapterPostsCard(getActivity(), list);
            recyclerViewPosts.setAdapter(adapterPostsCard);

        } else {
            Toast.makeText(mContext, "لاتوجد منشورات حاول مجددا", Toast.LENGTH_SHORT).show();
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
                            GetPosts(s_newsPaperId, s_category, Integer.parseInt(s_current_page) + 1 + "");}

            }
        });


    }

    private void GetSlider() {
        modelSliderImgArrayList = new ArrayList<>();
        modelSliderImgArrayList.clear();
//        progressLaySlider.setVisibility(View.VISIBLE);
        loadingLay.setVisibility(View.VISIBLE);
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

        Api.RetrofitGetSlider service = retrofit.create(Api.RetrofitGetSlider.class);

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
                                JSONObject item = jsonArrayData.getJSONObject(i);

                                ModelSliderImg modelSliderImg = new ModelSliderImg();

                                modelSliderImg.setId(item.getString("id"));
                                modelSliderImg.setTitle(item.getString("title"));
                                modelSliderImg.setDate(item.getString("published_at"));
                                modelSliderImg.setCategory("التصنيف: " + item.getString("category"));
                                modelSliderImg.setImg_url(item.getString("image"));
                                modelSliderImg.setNewsPaperId(item.getString("newspaper_id"));

                                //sub obj
                                JSONObject newsPaperItem = item.getJSONObject("newspaper");
                                modelSliderImg.setNewsPaperName(newsPaperItem.getString("name"));


                                modelSliderImgArrayList.add(modelSliderImg);

                            }
                            if (modelSliderImgArrayList.size() > 0) {
                                initSlider(modelSliderImgArrayList);
                            } else {
                                Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        default: {
                            Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
//                    progressLaySlider.setVisibility(View.GONE);
//                    loadingLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
//                progressLaySlider.setVisibility(View.GONE);
//                loadingLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(mContext, "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
//                progressLaySlider.setVisibility(View.GONE);
//                loadingLay.setVisibility(View.GONE);
            }
        });
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
                                Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        default: {
                            Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                    loadingLay.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                    noInternetContainer.setVisibility(View.VISIBLE);
                    swipeRefresh.setRefreshing(false);
                }
                progressLay.setVisibility(View.GONE);
                loadingLay.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(mContext, "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
                loadingLay.setVisibility(View.GONE);
                noInternetContainer.setVisibility(View.VISIBLE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }




    private void initSlider(ArrayList<ModelSliderImg> modelSliderImgArrayList) {
        if (modelSliderImgArrayList.size() > 0) {
            slideShow_adapter_main = new SlideShow_adapter_main(mContext, modelSliderImgArrayList);
            viewPager_slid_img.setAdapter(slideShow_adapter_main);
            indicator.setViewPager(viewPager_slid_img);
//            progressLaySlider.setVisibility(View.GONE);
            AutoSwipingImg();
        } else {
            Toast.makeText(mContext, "السلايدر فارغ", Toast.LENGTH_SHORT).show();
//            progressLaySlider.setVisibility(View.GONE);
        }
//        loadingLay.setVisibility(View.GONE);
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
                                if (list.isEmpty())
                                    initSpinnerPapers();
                            } else {
                                Toast.makeText(mContext, "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        default: {

                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    noInternetContainer.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(mContext, "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();
                noInternetContainer.setVisibility(View.VISIBLE);
            }
        });
    }


    private void GetAds1() {
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
        hashBody.put("position", "1");

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
                                adapter_ads1 = new SlideShow_adapter_ads(mContext,adsArrayList1);
                                viewPagerAds1.setAdapter(adapter_ads1);
                                AutoSwipingImgAds1();
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
            }
        });
    }
    private void GetAds2() {
        adsArrayList2=new ArrayList<>();
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
        hashBody.put("position", "2");

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
                                adsArrayList2.add(modelAds);
                            }
                            if (adsArrayList2.size()>0){
                                adapter_ads2 = new SlideShow_adapter_ads(mContext,adsArrayList2);
                                viewPagerAds2.setAdapter(adapter_ads2);
                                AutoSwipingImgAds2();
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
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


}
