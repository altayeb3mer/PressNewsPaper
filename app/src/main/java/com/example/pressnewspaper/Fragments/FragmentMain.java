package com.example.pressnewspaper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Adapter.SlideShow_adapter_main;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class FragmentMain extends Fragment {
    ViewPager viewPager_slid_img;
    SlideShow_adapter_main slideShow_adapter_main;
    ArrayList<ModelSliderImg> modelSliderImgArrayList;

    //spinner
    Spinner spinner1, spinner2;
    String[] arraySpinner1, arraySpinner2;
    ArrayAdapter<String> adapter1, adapter2;


    public FragmentMain() {
        // Required empty public constructor
    }

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        initSpinner();
        return view;
    }

    private void init() {
        viewPager_slid_img = view.findViewById(R.id.viewpager_slid_img);
        modelSliderImgArrayList = new ArrayList<>();
        indicator = view.findViewById(R.id.indicator);
        for (int i = 0; i <5 ; i++) {
            ModelSliderImg modelSliderImg = new ModelSliderImg();
            modelSliderImg.setTitle("السبت اول ايام شهر رمضان الكريم");
            modelSliderImg.setDate("الجمعة 24-4-2020");
            modelSliderImg.setCategory("التصنيف: اخبار");
            modelSliderImg.setImg_url("https://images.theabcdn.com/i/36187172");
            modelSliderImgArrayList.add(modelSliderImg);
        }

        if (modelSliderImgArrayList.size()>0){
            slideShow_adapter_main = new SlideShow_adapter_main(getContext(),modelSliderImgArrayList);
            viewPager_slid_img.setAdapter(slideShow_adapter_main);
            indicator.setViewPager(viewPager_slid_img);
            AutoSwipingImg();
        }
        testAdapter();
    }
    CircleIndicator indicator;
    Handler handler;
    Runnable runnable;
    Timer timer;

    private void AutoSwipingImg() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                int i = viewPager_slid_img.getCurrentItem();
                if (i==modelSliderImgArrayList.size()-1){
                    i=0;
                    viewPager_slid_img.setCurrentItem(i);
                }else{
                    i++;
                    viewPager_slid_img.setCurrentItem(i);
                }


            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 6000, 6000);
    }

    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    private void testAdapter(){
        recyclerViewPosts = view.findViewById(R.id.fragment_main_recycler);
        recyclerViewPosts.setNestedScrollingEnabled(false);
        postsCardArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelPostsCard modelPostsCard = new ModelPostsCard();
            if (i % 2 == 0){
                modelPostsCard.setTitle("دراسة بسنغافورة نهاية كورونا بالسودان يوم 4 من شهر مايو");
                modelPostsCard.setDate("الاربعاء 29-4-2020");
                modelPostsCard.setCategory("اخبار");
                modelPostsCard.setNewsPaperName("الانتباهة");
            }else{
                modelPostsCard.setTitle("ليونيل ميسي اتدرب يوميا واشتاق للكامب نو");
                modelPostsCard.setDate("الاربعاء 29-4-2020");
                modelPostsCard.setCategory("رياضة");
                modelPostsCard.setNewsPaperName("الهلال");
            }
            postsCardArrayList.add(modelPostsCard);
            adapterPostsCard = new AdapterPostsCard(getActivity(),postsCardArrayList);
            recyclerViewPosts.setAdapter(adapterPostsCard);
        }

        //end of test fun
    }

}
