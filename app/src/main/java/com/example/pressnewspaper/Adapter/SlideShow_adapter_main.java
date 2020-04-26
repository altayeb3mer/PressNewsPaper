package com.example.pressnewspaper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;


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

        ModelSliderImg modelSlideShowImg = new ModelSliderImg();
        modelSlideShowImg = modelSliderImgArrayList.get(position);

        textView_title.setText(modelSlideShowImg.getTitle());
        textView_date.setText(modelSlideShowImg.getDate());
        textView_category.setText(modelSlideShowImg.getCategory());


            Glide.with(context).load(modelSlideShowImg.getImg_url())
                    .into(imgView);


            container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
