package com.example.pressnewspaper.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pressnewspaper.Adapter.AdapterPostsCard;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.ToolbarClass;

import java.util.ArrayList;

public class PaperPostsActivity extends ToolbarClass {

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(R.layout.activity_paper_posts, "صحيفة الانتباهة");
        testAdapter();
    }
    RecyclerView recyclerViewPosts;
    AdapterPostsCard adapterPostsCard;
    ArrayList<ModelPostsCard> postsCardArrayList;
    private void testAdapter(){
        recyclerViewPosts = findViewById(R.id.paper_posts_recycler);
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
            adapterPostsCard = new AdapterPostsCard(this,postsCardArrayList);
            recyclerViewPosts.setAdapter(adapterPostsCard);
        }

        //end of test fun
    }

}
