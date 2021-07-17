package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

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

public class ConfirmActivity extends AppCompatActivity {

    EditText edtOtp;
    AppCompatButton button;
    LinearLayout progressLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = edtOtp.getText().toString().trim();
                if (!otp.isEmpty()){
                    verifyOtp(otp);
                }else{
                    ShowSnakBar("الرجاء كتابة كود التحقق");
                }
            }
        });


    }



    private void verifyOtp(String otp) {
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

        Api.RetrofitVerifyOtp service = retrofit.create(Api.RetrofitVerifyOtp.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("phone",getIntent().getExtras().getString("phone"));
        hashBody.put("otp",otp);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");

                    switch (statusCode){
                        case "200":{
                            JSONObject dataObject = object.getJSONObject("data");
                            boolean is_otp_correct = dataObject.getBoolean("is_otp_correct");
                            if (is_otp_correct){
                                boolean is_phone_registered = dataObject.getBoolean("is_phone_registered");
                                if (is_phone_registered){
                                    SharedPrefManager.getInstance(ConfirmActivity.this)
                                            .storeToken("Bearer"+" "+dataObject.getString("token"));
                                    Intent i = new Intent(ConfirmActivity.this,MainActivity.class);
                                    startActivity(i);
                                }else{
                                    Intent i = new Intent(ConfirmActivity.this,RegistrationActivity.class);
                                    i.putExtra("phone",getIntent().getExtras().getString("phone"));
                                    startActivity(i);
                                }

                                finish();

                            }else{
                                Toast.makeText(ConfirmActivity.this, "كود التحقق خاطئ", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        }
//                        case "401":{
//                            ShowSnakBar("تم استخدام البريد الالكتروني او رقم الهاتف مسبقا");
//                            break;
//                        }
                        default:{
//                            String msg = objectData.getString("message");
                            ShowSnakBar("حدث خطأ اثناء ارسال كود التحقق، حاول مجددا");
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    ShowSnakBar("بيانات الدخول غير صحيحة حاول مره اخرى");
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

    private void init() {
        edtOtp = findViewById(R.id.edtOtp);
        button = findViewById(R.id.btn);
        progressLay = findViewById(R.id.progressLay);
        container = findViewById(R.id.container);
    }

    RelativeLayout container;
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
