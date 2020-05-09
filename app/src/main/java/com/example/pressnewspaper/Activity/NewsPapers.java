package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pressnewspaper.Adapter.AdapterAllNewsPaper;
import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.ToolbarClass;

import java.util.ArrayList;


public class NewsPapers extends ToolbarClass {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_news_papers, "الصحف");
        testAdapter();
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
