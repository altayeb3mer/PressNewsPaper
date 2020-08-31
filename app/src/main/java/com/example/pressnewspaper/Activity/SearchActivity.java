package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

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

public class SearchActivity extends AppCompatActivity {

    ImageView imageView_back_icon,ic_clear;
    EditText editTextSearch;
    String query="";


    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;

    String s_current_page = "", s_last_page = "", s_perPage = "", s_newsPaperId = "", s_category = "";


    TextView noItemLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        postsCardArrayList = new ArrayList<>();
        imageView_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ic_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearch.setText("");
                showKeyboard(SearchActivity.this);
                query = "";
            }
        });
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    query = editTextSearch.getText().toString().trim();
                    if (!query.equals("")) {
                        hideKeyboard(SearchActivity.this);
                        s_current_page = "";
                        if (postsCardArrayList.size()>0){
                            postsCardArrayList.clear();
                            adapterPostsCard.notifyDataSetChanged();
                        }
                        searchFun(s_current_page);
                    } else {
                        Toast.makeText(SearchActivity.this, "الرجاء كتابة شي للبحث", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

    }


    GridLayoutManager gridLayoutManager;
    NestedScrollView nestedScrollView;
    LinearLayout progressLay;

    private void init() {
        noItemLay = findViewById(R.id.noItemLay);
        editTextSearch = findViewById(R.id.edtSearch);
        imageView_back_icon = findViewById(R.id.back_icon);
        ic_clear = findViewById(R.id.ic_clear);
        ic_clear.setVisibility(View.INVISIBLE);
        editTextSearch.addTextChangedListener(editTextWatcher);

        recyclerViewPosts = findViewById(R.id.recycler);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        nestedScrollView =findViewById(R.id.nestedScroll);
        progressLay = findViewById(R.id.progressLay);

    }

    private void searchFun(String currentPage) {
        progressLay.setVisibility(View.VISIBLE);
        noItemLay.setVisibility(View.GONE);
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
        hashMap.put("page", currentPage);
        hashMap.put("q", query);


        Api.RetrofitSearch service = retrofit.create(Api.RetrofitSearch.class);

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
                                noItemLay.setVisibility(View.GONE);
                            } else {
                                noItemLay.setVisibility(View.VISIBLE);
                            }

                            break;
                        }
                        default: {
                            Toast.makeText(getApplicationContext(), "تعذر الوصول للبيانات", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    progressLay.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "خطأ في التحويل حاول مرة اخري", Toast.LENGTH_SHORT).show();
                }
                progressLay.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "خطأ بالاتصال", Toast.LENGTH_SHORT).show();
                progressLay.setVisibility(View.GONE);
            }
        });
    }



    private void initPostAdapter(final ArrayList<ModelPostsCard> list) {
        recyclerViewPosts.setLayoutManager(gridLayoutManager);
        recyclerViewPosts.setNestedScrollingEnabled(false);

        if (list.size() > 0) {
            adapterPostsCard = new AdapterPostsCard(this, list);
            recyclerViewPosts.setAdapter(adapterPostsCard);

        } else {
            Toast.makeText(this, "لاتوجد منشورات حاول مجددا", Toast.LENGTH_SHORT).show();
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

                if (scrollY>0){
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i(TAG, "BOTTOM SCROLL");
                        if (Double.parseDouble(s_last_page) > Double.parseDouble(s_current_page))
                            searchFun(Integer.parseInt(s_current_page) + 1 + "");

                    }
                }
            }
        });


    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInput(editTextSearch, 0);
    }


    TextWatcher editTextWatcher = new TextWatcher() {
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

            if (check.length()>0){
                ic_clear.setVisibility(View.VISIBLE);
            }else{
                ic_clear.setVisibility(View.INVISIBLE);
            }

        }

    };

}
