package com.example.pressnewspaper.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class FragmentSavedPost extends Fragment {

    public FragmentSavedPost() {
        // Required empty public constructor
    }

    //spinner
    Spinner spinner1, spinner2;
    String[] arraySpinner1, arraySpinner2;
    ArrayAdapter<String> adapter1, adapter2;


    private void initSpinner(){
        //init spinner1
        spinner1 = view.findViewById(R.id.spinner1);
        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
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
        //init spinner2
        spinner2 = view.findViewById(R.id.spinner2);
        arraySpinner2 = new String[]{"كل التصنيفات", "تصنيف1", "تصنيف2", "تصنيف3"};
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

    View view;
    String token="";
    LinearLayout noItemLay, progressLay, notLoginLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_saved_post, container, false);
        init();
        token = SharedPrefManager.getInstance(mContext).GetToken();
        if (!token.equals("")){
            GetMySaved();
            notLoginLay.setVisibility(View.GONE);
        }else{
            notLoginLay.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void init() {
        notLoginLay = view.findViewById(R.id.notLoginLay);
        progressLay = view.findViewById(R.id.progressLay);
        noItemLay = view.findViewById(R.id.noItemLay);

    }


    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    private void initAdapter( ArrayList<ModelPostsCard> list){
        recyclerViewPosts = view.findViewById(R.id.fragment_saved_recycler);
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
//            postsCardArrayList.add(modelPostsCard);
//        }
            adapterPostsCard = new AdapterPostsCard(getActivity(),list);
            recyclerViewPosts.setAdapter(adapterPostsCard);

        //end of test fun
    }


    private void GetMySaved() {
        postsCardArrayList = new ArrayList<>();
        postsCardArrayList.clear();
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

        Api.RetrofitGetMySaved service = retrofit.create(Api.RetrofitGetMySaved.class);

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
                                noItemLay.setVisibility(View.GONE);
                            } else {
                                noItemLay.setVisibility(View.VISIBLE);
                            }

                            break;
                        }
                        default: {
                            Toast.makeText(mContext, "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "تعذر الوصول لسجل المحفوظات حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(mContext, "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }



    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;

    }
    
}
