package com.example.pressnewspaper.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Adapter.AdapterMySubscriptions;
import com.example.pressnewspaper.Model.ModelMySub;
import com.example.pressnewspaper.Model.ModelNewsPaper;
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


public class FragmentMySub extends Fragment {


    View view;
    LinearLayout noItemLay, progressLay, notLoginLay;
    RecyclerView recyclerView;
    AdapterMySubscriptions adapterMySubscriptions;
    ArrayList<ModelMySub> mySubArrayList;

    public FragmentMySub() {
        // Required empty public constructor
    }

    String token="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_my_sub, container, false);
        init();
        token = SharedPrefManager.getInstance(getContext()).GetToken();
        if (!token.equals("")){
            GetMySub();
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
        recyclerView = view.findViewById(R.id.spc_recycler);
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void initAdapter(ArrayList<ModelMySub> list) {
//        for (int i = 0; i < 10; i++) {
//            ModelMySub modelMySub = new ModelMySub();
//            if (i % 2 == 0){
//                modelMySub.setNewsPaperName("صحيفة الصيحة");
//                modelMySub.setSubscriptionsType("شهري");
//                modelMySub.setSubscriptionsStartDate("2-4-2020");
//                modelMySub.setSubscriptionsEndDate("2-5-2020");
//            }else{
//                modelMySub.setNewsPaperName("صحيفة الصيحة");
//                modelMySub.setSubscriptionsType("شهري");
//                modelMySub.setSubscriptionsStartDate("2-4-2020");
//                modelMySub.setSubscriptionsEndDate("2-5-2020");
//            }
//            mySubArrayList.add(modelMySub);
        adapterMySubscriptions = new AdapterMySubscriptions(getActivity(), list);
        recyclerView.setAdapter(adapterMySubscriptions);

        //end of test fun
    }


    private void GetMySub() {
        mySubArrayList = new ArrayList<>();
        mySubArrayList.clear();
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

        Api.RetrofitGetMySub service = retrofit.create(Api.RetrofitGetMySub.class);

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

                                ModelMySub modelMySub = new ModelMySub();
                                modelMySub.setNewsPaperId(item.getString("name"));
                                modelMySub.setNewsPaperName(item.getString("name"));
                                modelMySub.setSubscriptionsType(item.getString("subscription_name"));
                                modelMySub.setSubscriptionsStartDate(item.getString("sub_start_date"));
                                modelMySub.setSubscriptionsEndDate(item.getString("sub_end_date"));

                                mySubArrayList.add(modelMySub);

                            }
                            if (mySubArrayList.size() > 0) {
                                initAdapter(mySubArrayList);
                                noItemLay.setVisibility(View.GONE);
                            } else {
                                noItemLay.setVisibility(View.VISIBLE);
                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "تعذر الوصول لسجل الصحف حاول مجددا", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getContext(), "خطأ في الاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }

}
