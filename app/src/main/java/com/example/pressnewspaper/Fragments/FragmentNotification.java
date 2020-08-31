package com.example.pressnewspaper.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;

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


public class FragmentNotification extends Fragment {
    //spinner
    Spinner spinner1;
    String[] arraySpinner1;
    ArrayAdapter<String> adapter1;


    LinearLayout muteLay;
    View view;
    LinearLayout progressLay, noItemLay;
    RelativeLayout relativeLayout;
    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    AppCompatButton buttonShowMore;
    String s_current_page = "", s_last_page = "", s_perPage = "";
    public FragmentNotification() {
        // Required empty public constructor
    }

    private void initSpinner() {
        //init spinner1
        spinner1 = view.findViewById(R.id.spinner1);
        arraySpinner1 = new String[]{"كل الاشعارات", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, arraySpinner1) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        init();
        initSpinner();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    SwipeRefreshLayout swipeRefresh;
    private void init() {
        postsCardArrayList=new ArrayList<>();
        swipeRefresh= view.findViewById(R.id.swipeRefresh);
        nestedScrollView = view.findViewById(R.id.nestedScroll);
        postsCardArrayList = new ArrayList<>();
        buttonShowMore = view.findViewById(R.id.btn);
        progressLay = view.findViewById(R.id.progressLay);
        noItemLay = view.findViewById(R.id.noItemLay);
        recyclerViewPosts = view.findViewById(R.id.notification_recycler);
        relativeLayout = view.findViewById(R.id.above_container);
        muteLay = view.findViewById(R.id.muteLay);
        if (SharedPrefManager.getInstance(mContext).receiveNotification()) {
            relativeLayout.setVisibility(View.GONE);
            recyclerViewPosts.setVisibility(View.VISIBLE);
            muteLay.setVisibility(View.GONE);
            //getPost must call here
            GetPosts(s_current_page);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recyclerViewPosts.setVisibility(View.GONE);
            muteLay.setVisibility(View.VISIBLE);
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
                        GetPosts(Integer.parseInt(s_current_page) + 1 + "");

                }
            }
        });






        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_current_page="1";
                swipeRefresh.setRefreshing(true);
                if (SharedPrefManager.getInstance(mContext).receiveNotification()) {
                    postsCardArrayList=new ArrayList<>();
                    if (adapterPostsCard!=null){
                        adapterPostsCard = new AdapterPostsCard(getActivity(),postsCardArrayList);
                        recyclerViewPosts.setAdapter(adapterPostsCard);
                    }
                    relativeLayout.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.VISIBLE);
                    muteLay.setVisibility(View.GONE);
                    //getPost must call here
                    GetPosts(s_current_page);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.GONE);
                    muteLay.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    private void initAdapter(ArrayList<ModelPostsCard> list) {

        if (list.size() > 0) {
            adapterPostsCard = new AdapterPostsCard(getActivity(), list);
            recyclerViewPosts.setAdapter(adapterPostsCard);
//            if (Double.parseDouble(s_last_page) > Double.parseDouble(s_current_page)) {
//                buttonShowMore.setVisibility(View.VISIBLE);
//            } else {
//                buttonShowMore.setVisibility(View.GONE);
//            }
        } else {
            Toast.makeText(mContext, "لاتوجد منشورات حاول مجددا", Toast.LENGTH_SHORT).show();
            noItemLay.setVisibility(View.VISIBLE);
        }
        if (Integer.parseInt(s_last_page) > Integer.parseInt(s_current_page)) {
            recyclerViewPosts.scrollTo(list.size() - Integer.parseInt(s_perPage), list.size());
            recyclerViewPosts.scrollToPosition(list.size() - Integer.parseInt(s_perPage));
            adapterPostsCard.notifyItemInserted(list.size() - Integer.parseInt(s_perPage));
        }


//        buttonShowMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GetPosts(Integer.parseInt(s_current_page) + 1 + "");
//            }
//        });


        //end of test fun
    }

    private void GetPosts(String currentPage) {
        progressLay.setVisibility(View.VISIBLE);
        buttonShowMore.setVisibility(View.GONE);
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
        hashMap.put("page", currentPage);

        Api.RetrofitGetUrgentPost service = retrofit.create(Api.RetrofitGetUrgentPost.class);

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
                                initAdapter(postsCardArrayList);
                            } else {
                                Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                                noItemLay.setVisibility(View.VISIBLE);

                            }

                            swipeRefresh.setRefreshing(false);
                            break;
                        }
                        default: {
                            Toast.makeText(mContext, "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
                progressLay.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(mContext, "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }



    NestedScrollView nestedScrollView;
    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;

    }
    
    
}
