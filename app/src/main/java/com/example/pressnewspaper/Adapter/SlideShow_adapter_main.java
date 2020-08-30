package com.example.pressnewspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Activity.NewsPaperDetails;
import com.example.pressnewspaper.Activity.PostDetailsActivity;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;


import java.util.ArrayList;

/**
 * Created by Altayeb on 10/25/2018.
 */
public class SlideShow_adapter_main extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;



    public ArrayList<ModelSliderImg> modelSliderImgArrayList;
    int detail;


    public SlideShow_adapter_main(Context context, ArrayList<ModelSliderImg> arrayList) {
        this.context = context;

        this.modelSliderImgArrayList=arrayList;
        //this.detail=det;
      }





    @Override
    public int getCount() {

            return modelSliderImgArrayList.size();
        }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.slider_img_main,container,false);
        TextView textView_title = view.findViewById(R.id.title);
        TextView textView_date = view.findViewById(R.id.date);
        TextView textView_category = view.findViewById(R.id.category);
        ImageView imgView=view.findViewById(R.id.image_view);
        AppCompatButton button = view.findViewById(R.id.btn);
        RelativeLayout relativeLayoutContainer = view.findViewById(R.id.container);


        ModelSliderImg modelSlideShowImg = new ModelSliderImg();
        modelSlideShowImg = modelSliderImgArrayList.get(position);

        textView_title.setText(modelSlideShowImg.getTitle());
        textView_date.setText(modelSlideShowImg.getDate());
        textView_category.setText(modelSlideShowImg.getCategory());
        button.setText(modelSlideShowImg.getNewsPaperName());


        final ModelSliderImg finalModelSlideShowImg = modelSlideShowImg;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsPaperDetails.class);
                intent.putExtra("id",finalModelSlideShowImg.getNewsPaperId());
                context.startActivity(intent);
            }
        });
        relativeLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("id", finalModelSlideShowImg.getId());
                context.startActivity(intent);
            }
        });

            Glide.with(context).load(Api.ROOT_URL+"storage/"+modelSlideShowImg.getImg_url())
                    .into(imgView);


            container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
