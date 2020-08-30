package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.Model.ModelSub;
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

public class NewSub extends ToolbarClass {

    String id="",paperName="",subId="",s_price="";
    //spinner
    Spinner spinner1;
    ArrayList<ModelSub> modelSubArrayList;
    ArrayAdapter<String> adapter1;

    AppCompatButton button;

    TextView textViewPaperName, textViewPrice;
    LinearLayout priceLay;

    private void initSpinnerSub() {
        //init spinner1
        ArrayList<String> list = new ArrayList<>();
        list.add("اختر اشتراك..");
        for (int i = 0; i < modelSubArrayList.size(); i++) {
            list.add(modelSubArrayList.get(i).getName());
        }
        spinner1 = findViewById(R.id.spinner);
//        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list) {
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
                    subId = "";
                    priceLay.setVisibility(View.INVISIBLE);
                } else {
                    subId = modelSubArrayList.get(position-1).getId();
                    priceLay.setVisibility(View.VISIBLE);
                    textViewPrice.setText(modelSubArrayList.get(position-1).getPrice()+" "+"ج س");
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_new_sub, "الاشتراك في صحيفة");

        init();
        Bundle args = getIntent().getExtras();
        if (args!=null){
            id = args.getString("id");
            paperName = args.getString("paperName");
            textViewPaperName.setText(paperName);
        }else{
            finish();
            Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
        }


        if (isOnline()) {
            GetAllSub();
        } else {
            Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void init() {
        textViewPaperName = findViewById(R.id.paperName);
        textViewPrice = findViewById(R.id.price);
        priceLay = findViewById(R.id.priceLay);
        progressLay = findViewById(R.id.progressLay);

        button = findViewById(R.id.btn);
        token = SharedPrefManager.getInstance(getApplicationContext()).GetToken();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!id.equals("")&&!subId.equals("")){
                    if (!token.equals("")){
                        subscribeFun();
                    }else{
                        dialogMessage("مستخدم جديد","لم تقم بالتسجيل..الرجاء التسجيل والمحاوله مرة اخرى");
                    }
                }else{
                    dialogMessage("خطأ","الرجاء اختيار نوع الاشتراك");
                }
            }
        });

    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    LinearLayout progressLay;

    private void GetAllSub() {
        modelSubArrayList = new ArrayList<>();
        modelSubArrayList.clear();
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
//                        ongoing.addHeader("Content-Type", "application/json;");
//                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

//                        ongoing.addHeader("Authorization",token);

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

        Api.RetrofitGetAllSub service = retrofit.create(Api.RetrofitGetAllSub.class);

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

                                ModelSub modelSub = new ModelSub();
                                modelSub.setId(item.getString("id"));
                                modelSub.setName(item.getString("name"));
                                modelSub.setPrice(item.getString("price"));

                                modelSubArrayList.add(modelSub);

                            }
                            if (modelSubArrayList.size() > 0) {
                                initSpinnerSub();
                            } else {
                                finish();
                                Toast.makeText(getApplicationContext(), "الاشتراكات غير متاحه الان حاول مره اخرى", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "حدث خطأ حاول مجددا", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "تعذر الوصول لسجل المحفوظات حاول مرة اخرى", Toast.LENGTH_SHORT).show();
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

    String token="";

    private void subscribeFun() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

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

        Api.RetrofitSubscribe service = retrofit.create(Api.RetrofitSubscribe.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("newspaper_id",id);
        hashBody.put("subscription_id",subId);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    JSONObject data = object.getJSONObject("data");
                    switch (statusCode){
                        case "200":{
                            dialogMessage("عملية اشتراك",data.getString("message"));
                            break;
                        }

                    }

                } catch (Exception e) {
                    dialogMessage("خطأ","حدث خطأ الرجاء المحاولة مره اخرى");
                    e.printStackTrace();

                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                dialogMessage("خطأ","حدث خطأ الرجاء المحاولة مره اخرى");
                progressLay.setVisibility(View.GONE);
            }
        });
    }



    private void dialogMessage(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(NewSub.this).create();
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
