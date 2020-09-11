package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;
import com.example.pressnewspaper.Utils.ToolbarClass;

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

public class NewsPaperDetails extends ToolbarClass {

    LinearLayout progressLay;
    RelativeLayout container;
    TextView textViewName, textViewCategory, textViewPublishPeriod, textViewPublishOn;
    ImageView imageView;
    AppCompatButton button;

    String id = "", paperName="";



    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_news_paper_details, "تفاصيل صحيفة");

        Bundle args = getIntent().getExtras();
        if (args!=null){
            id = args.getString("id");
        }else{
            finish();
            Toast.makeText(getApplicationContext(), "حدث خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
        }

        init();

        if (isOnline()) {
            GetPaperDetails();
        } else {
            Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void init() {
        publisDayLay = findViewById(R.id.publisDayLay);
        progressLay = findViewById(R.id.progressLay);
        container = findViewById(R.id.container);
        imageView = findViewById(R.id.img);
        button = findViewById(R.id.btn);

        textViewName = findViewById(R.id.name);
        textViewCategory = findViewById(R.id.category);
        textViewPublishPeriod = findViewById(R.id.textViewPublishPeriod);
        textViewPublishOn = findViewById(R.id.textViewPublishOn);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsPaperDetails.this,NewSub.class);
                intent.putExtra("id",id);
                intent.putExtra("paperName",paperName);
                startActivity(intent);
            }
        });

    }


    CardView publisDayLay;
    private void GetPaperDetails() {
        progressLay.setVisibility(View.VISIBLE);
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



        Api.RetrofitGetNewsPaperDetails service = retrofit.create(Api.RetrofitGetNewsPaperDetails.class);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);

        Call<String> call = service.putParam(hashMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status_code = object.getString("status_code");


                    switch (status_code) {
                        case "200": {
                            JSONObject data = object.getJSONObject("data");
//                            JSONObject sub1 = data.getJSONObject("1");


                            paperName = data.getString("name");
                            textViewName.setText(paperName);
                            textViewCategory.setText(data.getString("type_name"));
                            textViewPublishPeriod.setText(data.getString("publish_period"));

                            String publish_day = data.getString("publish_day");
                            if (!publish_day.equals("")&&!publish_day.equals("null")){
                                textViewPublishOn.setText(publish_day);
                                publisDayLay.setVisibility(View.VISIBLE);
                            }else{
                                publisDayLay.setVisibility(View.GONE);
                            }



                            Glide.with(NewsPaperDetails.this).load(Api.ROOT_URL + "storage/" + data.getString("logo"))
                                    .into(imageView);

                            container.setVisibility(View.VISIBLE);
                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
