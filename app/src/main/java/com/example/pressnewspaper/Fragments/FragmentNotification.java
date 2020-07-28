package com.example.pressnewspaper.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;

import java.util.ArrayList;


public class FragmentNotification extends Fragment {
    //spinner
    Spinner spinner1;
    String[] arraySpinner1;
    ArrayAdapter<String> adapter1;



    private void initSpinner(){
        //init spinner1
        spinner1 = view.findViewById(R.id.spinner1);
        arraySpinner1 = new String[]{"كل الاشعارات", "صحيفة1", "صحيفة2", "صحيفة3"};
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
    }
    public FragmentNotification() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_notification, container, false);
        testAdapter();
        initSpinner();
        return view;
    }


    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    private void testAdapter(){
        recyclerViewPosts = view.findViewById(R.id.notification_recycler);
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
