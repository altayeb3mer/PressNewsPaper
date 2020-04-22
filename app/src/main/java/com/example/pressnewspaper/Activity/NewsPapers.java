package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.ToolbarClass;


public class NewsPapers extends ToolbarClass {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_news_papers, "الصحف");

    }

}
