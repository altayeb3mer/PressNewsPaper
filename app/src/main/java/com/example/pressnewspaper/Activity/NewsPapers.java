package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.pressnewspaper.Adapter.AdapterAllNewsPaper;
import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.ToolbarClass;

import java.util.ArrayList;


public class NewsPapers extends ToolbarClass {


    //spinner
    Spinner spinner1;
    String[] arraySpinner1;
    ArrayAdapter<String> adapter1;

    private void initSpinner(){
        //init spinner1
        spinner1 = findViewById(R.id.spinner1);
        arraySpinner1 = new String[]{"كل الصحف", "صحيفة1", "صحيفة2", "صحيفة3"};
        adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner1) {
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

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_news_papers, "الصحف");
        testAdapter();
        initSpinner();
    }
    RecyclerView recyclerView;
    AdapterAllNewsPaper adapterAllNewsPaper;
    ArrayList<ModelNewsPaper> newsPaperArrayList;
    private void testAdapter(){
        recyclerView = findViewById(R.id.news_paper_recycler);
        recyclerView.setNestedScrollingEnabled(false);
        newsPaperArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelNewsPaper modelNewsPaper = new ModelNewsPaper();
            if (i % 2 == 0){
                modelNewsPaper.setNewPaperName("صحيفة الصحافة");
                modelNewsPaper.setNewPaperType("سياسية");
                modelNewsPaper.setReleaseType("اسبوعية");
                modelNewsPaper.setReleaseTime("تصدر يوم الاحد");
            }else{
                modelNewsPaper.setNewPaperName("صحيفة الدار");
                modelNewsPaper.setNewPaperType("اخبار");
                modelNewsPaper.setReleaseType("يومية");
                modelNewsPaper.setReleaseTime("تصدر الساعة 8 صباحة");
            }
            newsPaperArrayList.add(modelNewsPaper);
            adapterAllNewsPaper = new AdapterAllNewsPaper(this,newsPaperArrayList);
            recyclerView.setAdapter(adapterAllNewsPaper);
        }

        //end of test fun
    }

}
