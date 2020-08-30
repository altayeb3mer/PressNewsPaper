package com.example.pressnewspaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Activity.PostDetailsActivity;
import com.example.pressnewspaper.Model.ModelAds;
import com.example.pressnewspaper.Model.ModelSliderImg;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

import java.util.ArrayList;

/**
 * Created by Altayeb on 10/25/2018.
 */
public class SlideShow_adapter_ads extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;



    public ArrayList<ModelAds> modelAdsArrayList;
    int detail;


    public SlideShow_adapter_ads(Context context, ArrayList<ModelAds> modelAdsArrayList) {
        this.context = context;

        this.modelAdsArrayList=modelAdsArrayList;
        //this.detail=det;
      }





    @Override
    public int getCount() {

            return modelAdsArrayList.size();
        }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.ads_slider_lay,container,false);

        ImageView imgView=view.findViewById(R.id.img);

        ModelAds item = modelAdsArrayList.get(position);


            Glide.with(context).load(Api.ROOT_URL+"storage/"+item.getImgUrl())
                    .into(imgView);


            container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
