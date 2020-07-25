package com.example.pressnewspaper.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ToolbarClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegistrationActivity extends ToolbarClass {
    TextInputEditText editTextName, editTextEmail, editTextPass1, editTextPass2;
    AppCompatButton button;
    LinearLayout progressLay;
    String s_name = "", s_email = "", s_pass1 = "", s_pass2 = "";
    RelativeLayout container;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_registration, "انشاء حساب");
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    PreRregistration();
                } else {
                    ShowSnakBar("تعذر الاتصال بالانترنت");
                }
            }
        });
        progressLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegistrationActivity.this, "جاري التحميل..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PreRregistration() {
        s_name = editTextName.getText().toString().trim();
        s_email = editTextEmail.getText().toString().trim();
        s_pass1 = editTextPass1.getText().toString().trim();
        s_pass2 = editTextPass2.getText().toString().trim();
        if (!s_name.equals("") || !s_email.equals("") || !s_pass1.equals("") || !s_pass2.equals("")) {
            if (s_pass1.equals(s_pass2)){
                //do register
                DoRegister();
            }else{
                ShowSnakBar("كلمة السر غير متطابقة");
            }
        } else {
            ShowSnakBar("الرجاء تعبئة كل الحقول");
        }
    }

    private void DoRegister() {
        progressLay.setVisibility(View.VISIBLE);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Content-Type", "application/json;");
                        ongoing.addHeader("Content-Type", "application/x-www-form-urlencoded");

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

        Api.RetrofitRegister service = retrofit.create(Api.RetrofitRegister.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("name",s_name);
        hashBody.put("email_or_phone",s_email);
        hashBody.put("password",s_pass1);
        hashBody.put("c_password",s_pass2);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");
                    switch (statusCode){
                        case "200":{
                            String data = object.getString("data");
                            JSONObject objectData=new JSONObject(data);
                            String token = objectData.getString("token");

                            SharedPrefManager.getInstance(RegistrationActivity.this).storeToken(token);

                            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));

                            break;
                        }
//                        case "401":{
//                            ShowSnakBar("تم استخدام البريد الالكتروني او رقم الهاتف مسبقا");
//                            break;
//                        }
                        default:{
                            String msg = object.getString("message");
                            ShowSnakBar(msg);
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ShowSnakBar("خطأ في التحويل");
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                ShowSnakBar("خطأ بالانترنت");
                progressLay.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        container = findViewById(R.id.container);

        editTextName = findViewById(R.id.edt_name);
        editTextEmail = findViewById(R.id.edt_email);
        editTextPass1 = findViewById(R.id.edt_pass1);
        editTextPass2 = findViewById(R.id.edt_pass2);

        button = findViewById(R.id.btn);
        progressLay = findViewById(R.id.progressLay);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void ShowSnakBar(String msg) {
        Snackbar snackbar = Snackbar.make(container, msg, Snackbar.LENGTH_LONG);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(Color.RED);
        TextView masseage;
        masseage = snackview.findViewById(R.id.snackbar_text);
        masseage.setTextSize(14);
        masseage.setTextColor(Color.WHITE);
        snackbar.show();
    }

}
