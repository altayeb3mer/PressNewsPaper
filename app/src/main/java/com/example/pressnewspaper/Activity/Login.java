package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.SharedPrefManager;
import com.example.pressnewspaper.Utils.ToolbarClass;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends ToolbarClass {
    EditText editTextEmailPhone, editTextPass;
    String s_emailPhone="", s_pass="";
    CardView button;
    LinearLayout progressLay;
    TextView register;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.login_design, "تسجيل دخول");

        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    PreLogin();
                } else {
                    ShowSnakBar("تعذر الاتصال بالانترنت");
                }
            }
        });
    }

    private void PreLogin() {
        s_emailPhone = editTextEmailPhone.getText().toString().trim();
//        s_pass = editTextPass.getText().toString().trim();
        if (!s_emailPhone.equals("")){
            LoginFun();
        }else{
            ShowSnakBar("الرجاء كتابة رقم الهاتف");
        }

    }

    private void LoginFun() {
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

        Api.RetrofitSendOtp service = retrofit.create(Api.RetrofitSendOtp.class);

        HashMap<String, String> hashBody = new HashMap<>();
        hashBody.put("phone",s_emailPhone);
//        hashBody.put("password",s_pass);

        Call<String> call = service.putParam(hashBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String statusCode = object.getString("status_code");

//                    String data = object.getString("data");
//                    JSONObject objectData=new JSONObject(data);
                    switch (statusCode){
                        case "200":{
                            Intent intent = new Intent(getApplicationContext(),ConfirmActivity.class);
                            intent.putExtra("phone",s_emailPhone);
                            startActivity(intent);

                            finish();
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
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });
        container = findViewById(R.id.container);
        progressLay = findViewById(R.id.progressLay);
        editTextEmailPhone = findViewById(R.id.email_or_phone);
//        editTextEmailPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextPass = findViewById(R.id.password);

        editTextEmailPhone.addTextChangedListener(emailOrPhoneWatcher);

        button = findViewById(R.id.btn);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    CoordinatorLayout container;
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

    TextWatcher emailOrPhoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            String check = s.toString();

            if (!isValidEmail(check)&&!isValidMobile2(check)){
                editTextEmailPhone.setError("رقم هاتف غير صحيح");
            }

        }

    };

    public final static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isValidMobile(String phone) {
        if(Pattern.matches("[0-9]", phone)) {
            return phone.length() == 10;
        }
        return false;
    }
    private boolean isValidMobile2(String phone) {
        if (phone.length()==10){
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
        return false;
    }

}
