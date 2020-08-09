package com.example.pressnewspaper.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Adapter.SlideShow_adapter_main;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;

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


public class FragmentMain extends Fragment {
    ViewPager viewPager_slid_img;
    SlideShow_adapter_main slideShow_adapter_main;
    ArrayList<ModelSliderImg> modelSliderImgArrayList;

    //spinner
    Spinner spinner1, spinner2;
    String[] arraySpinner1, arraySpinner2;
    ArrayAdapter<String> adapter1, adapter2;
    AppCompatButton buttonShowMore;
    View view;
    LinearLayout progressLay, progressLaySlider;
    CircleIndicator indicator;
    Handler handler;
    Runnable runnable;
    Timer timer;
    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    String s_current_page = "", s_last_page = "", s_perPage = "", s_newsPaperId = "", s_category = "";
    ArrayList<ModelNewsPaper> newsPaperArrayList;
    LinearLayout spinner1Lay;


    public FragmentMain() {
        // Required empty public constructor
    }
    ArrayList<String> list;
    private void initSpinnerPapers() {
        //init spinner1
        list.add("عرض بالصحف");
        list.add("كل الصحف");
        for (int i = 0; i < newsPaperArrayList.size(); i++) {
            list.add(newsPaperArrayList.get(i).getNewPaperName());
        }
        spinner1Lay.setVisibility(View.VISIBLE);
        spinner1 = view.findViewById(R.id.spinner1);
//        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list) {
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
                    s_newsPaperId = newsPaperArrayList.get(position-2).getNewPaperId();
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

    private void initSpinner() {

        //init spinner2
        spinner2 = view.findViewById(R.id.spinner2);
        arraySpinner2 = new String[]{"عرض بالتصنيف","كل التصنيفات", "أخبار", "أعمدة ومقالات", "تقارير وتحقيقات","حوارات","ثقافة ومنوعات"};
        adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arraySpinner2) {
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
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {
                    s_category = "";
                    postsCardArrayList.clear();
                    if (adapterPostsCard != null)
                        adapterPostsCard.notifyDataSetChanged();
                    GetPosts(s_newsPaperId, s_category, "");
                } else {
                    s_category = arraySpinner2[position];
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        list = new ArrayList<>();
        init();
        initSpinner();
        GetSlider();
        GetPosts(s_newsPaperId, s_category, s_current_page);
        if (list.isEmpty())
        GetNewsPaper();
        return view;
    }

    private void init() {
        spinner1Lay = view.findViewById(R.id.spinner1Lay);
        spinner1Lay.setVisibility(View.GONE);
        postsCardArrayList = new ArrayList<>();
        buttonShowMore = view.findViewById(R.id.btn);
        progressLay = view.findViewById(R.id.progressLay);
        progressLaySlider = view.findViewById(R.id.progressLaySlider);

        viewPager_slid_img = view.findViewById(R.id.viewpager_slid_img);
        modelSliderImgArrayList = new ArrayList<>();
        indicator = view.findViewById(R.id.indicator);
//        for (int i = 0; i <5 ; i++) {
//            ModelSliderImg modelSliderImg = new ModelSliderImg();
//            modelSliderImg.setTitle("السبت اول ايام شهر رمضان الكريم");
//            modelSliderImg.setDate("الجمعة 24-4-2020");
//            modelSliderImg.setCategory("التصنيف: اخبار");
//            modelSliderImg.setImg_url("https://images.theabcdn.com/i/36187172");
//            modelSliderImgArrayList.add(modelSliderImg);
//        }


    }

    //end of test fun

    private void AutoSwipingImg() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                int i = viewPager_slid_img.getCurrentItem();
                if (i == modelSliderImgArrayList.size() - 1) {
                    i = 0;
                    viewPager_slid_img.setCurrentItem(i);
                } else {
                    i++;
                    viewPager_slid_img.setCurrentItem(i);
                }


            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 10000, 6000);
    }

    private void initPostAdapter(ArrayList<ModelPostsCard> list) {
        recyclerViewPosts = view.findViewById(R.id.fragment_main_recycler);
        recyclerViewPosts.setNestedScrollingEnabled(false);
//        postsCardArrayList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ModelPostsCard modelPostsCard = new ModelPostsCard();
//            if (i % 2 == 0){
//                modelPostsCard.setTitle("دراسة بسنغافورة نهاية كورونا بالسودان يوم 4 من شهر مايو");
//                modelPostsCard.setDate("الاربعاء 29-4-2020");
//                modelPostsCard.setCategory("اخبار");
//                modelPostsCard.setNewsPaperName("الانتباهة");
//            }else{
//                modelPostsCard.setTitle("ليونيل ميسي اتدرب يوميا واشتاق للكامب نو");
//                modelPostsCard.setDate("الاربعاء 29-4-2020");
//                modelPostsCard.setCategory("رياضة");
//                modelPostsCard.setNewsPaperName("الهلال");
//            }
        if (list.size() > 0) {
            adapterPostsCard = new AdapterPostsCard(getActivity(), list);
            recyclerViewPosts.setAdapter(adapterPostsCard);
            if (Double.parseDouble(s_last_page) > Double.parseDouble(s_current_page)) {
                buttonShowMore.setVisibility(View.VISIBLE);
            } else {
                buttonShowMore.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getContext(), "لاتوجد منشورات حاول مجددا", Toast.LENGTH_SHORT).show();
        }

        if (Integer.parseInt(s_last_page) > Integer.parseInt(s_current_page)) {
            recyclerViewPosts.scrollTo(list.size() - Integer.parseInt(s_perPage), list.size());
            recyclerViewPosts.scrollToPosition(list.size() - Integer.parseInt(s_perPage));
            adapterPostsCard.notifyItemInserted(list.size() - Integer.parseInt(s_perPage));
        }


        buttonShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPosts(s_newsPaperId, s_category, Integer.parseInt(s_current_page) + 1 + "");
            }
        });

    }

    private void GetSlider() {
        modelSliderImgArrayList = new ArrayList<>();
        modelSliderImgArrayList.clear();
        progressLaySlider.setVisibility(View.VISIBLE);
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
                                Toast.makeText(getActivity(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getActivity(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLaySlider.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
                progressLaySlider.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getActivity(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLaySlider.setVisibility(View.GONE);
            }
        });
    }

    private void GetPosts(String newsPaperId, String category, String currentPage) {
        progressLay.setVisibility(View.VISIBLE);
        buttonShowMore.setVisibility(View.GONE);
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
                                Toast.makeText(getActivity(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();

                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getActivity(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void initSlider(ArrayList<ModelSliderImg> modelSliderImgArrayList) {
        if (modelSliderImgArrayList.size() > 0) {
            slideShow_adapter_main = new SlideShow_adapter_main(getContext(), modelSliderImgArrayList);
            viewPager_slid_img.setAdapter(slideShow_adapter_main);
            indicator.setViewPager(viewPager_slid_img);
            progressLaySlider.setVisibility(View.GONE);
            AutoSwipingImg();
        } else {
            Toast.makeText(getContext(), "السلايدر فارغ", Toast.LENGTH_SHORT).show();
            progressLaySlider.setVisibility(View.GONE);
        }
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
                                Toast.makeText(getContext(), "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "تعذر الوصول للصحف", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
