package com.example.pressnewspaper.Fragments;

import android.content.Context;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Activity.DeptPostsActivity;
import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelNewsPaper;
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

public class FragmentDepts extends Fragment implements View.OnClickListener {


    RelativeLayout cardView1, cardView2, cardView3, cardView4, cardView5;
    View view;




    public FragmentDepts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_depts, container, false);
        init();
        return view;
    }
    private void init() {

        cardView1 = view.findViewById(R.id.card1);
        cardView2 = view.findViewById(R.id.card2);
        cardView3 = view.findViewById(R.id.card3);
        cardView4 = view.findViewById(R.id.card4);
        cardView5 = view.findViewById(R.id.card5);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);

    }





    Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card1:{
                Intent intent = new Intent(getActivity(), DeptPostsActivity.class);
                intent.putExtra("title","أخبار");
                intent.putExtra("category","أخبار");
                intent.putExtra("paperId","");
                getActivity().startActivity(intent);
                break;
            }
            case R.id.card2:{
                Intent intent = new Intent(getActivity(), DeptPostsActivity.class);
                intent.putExtra("title","تقارير وتحقيقات");
                intent.putExtra("category","تقارير وتحقيقات");
                intent.putExtra("paperId","");
                getActivity().startActivity(intent);
                break;
            }
            case R.id.card3:{
                Intent intent = new Intent(getActivity(), DeptPostsActivity.class);
                intent.putExtra("title","حوارات");
                intent.putExtra("category","حوارات");
                intent.putExtra("paperId","");
                getActivity().startActivity(intent);
                break;
            }
            case R.id.card4:{
                Intent intent = new Intent(getActivity(), DeptPostsActivity.class);
                intent.putExtra("title","ثقافة ومنوعات");
                intent.putExtra("category","ثقافة ومنوعات");
                intent.putExtra("paperId","");
                getActivity().startActivity(intent);
                break;
            }
            case R.id.card5:{
                Intent intent = new Intent(getActivity(), DeptPostsActivity.class);
                intent.putExtra("title","أعمدة ومقالات");
                intent.putExtra("category","أعمدة ومقالات");
                intent.putExtra("paperId","");
                getActivity().startActivity(intent);
                break;
            }
        }
    }
}