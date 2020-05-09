package com.example.pressnewspaper.Activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Adapter.AdapterOtherPosts;
import com.example.pressnewspaper.Model.ModelOtherPosts;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.ToolbarClass;

import java.util.ArrayList;

public class PostDetailsActivity extends ToolbarClass {

    RecyclerView recyclerView;
    AdapterOtherPosts adapterOtherPosts;
    ArrayList<ModelOtherPosts> otherPostsArrayList;

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_post_details, " ");
        testAdapter();
    }

    private void testAdapter() {
        recyclerView = findViewById(R.id.post_details_recycler);
        otherPostsArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ModelOtherPosts otherPosts = new ModelOtherPosts();
            if (i % 2 == 0) {
                otherPosts.setNewsPaperName("الانتباهة");
                otherPosts.setTitle("43 اصابة جديدة بفيروس كرونا");
            } else {
                otherPosts.setNewsPaperName("الصيحة");
                otherPosts.setTitle("وزير الصحة الوضع غير مطمئن وصلنا مرحلة الخطر");
            }
            otherPostsArrayList.add(otherPosts);
            adapterOtherPosts = new AdapterOtherPosts(this, otherPostsArrayList);
            recyclerView.setAdapter(adapterOtherPosts);
        }

        //end of test fun
    }

}
