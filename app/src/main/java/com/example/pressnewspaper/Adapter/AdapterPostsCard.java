package com.example.pressnewspaper.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Activity.NewsPaperDetails;
import com.example.pressnewspaper.Activity.PostDetailsActivity;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

import java.util.ArrayList;


public class AdapterPostsCard extends RecyclerView.Adapter<AdapterPostsCard.ViewHolder> {

    ArrayList<ModelPostsCard> modelPostsCardArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    public AdapterPostsCard(Activity activity, ArrayList<ModelPostsCard> modelPostsCardArrayList) {
        this.mInflater = LayoutInflater.from(activity);
        this.modelPostsCardArrayList = modelPostsCardArrayList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_rec_items, parent, false);



        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelPostsCard item = modelPostsCardArrayList.get(position);

        holder.buttonNewsPaper.setText(item.getNewsPaperName());
        holder.textView_title.setText(item.getTitle());
        holder.textView_date.setText(item.getDate());
        holder.textView_category.setText("التصنيف:"+" "+item.getCategory());

        Glide.with(activity).load(Api.ROOT_URL+"storage/"+item.getImg_url())
                .into(holder.imageView);

        holder.cardView_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                activity.startActivity(new Intent(activity, PostDetailsActivity.class));
                Intent intent = new Intent(activity, PostDetailsActivity.class);
                intent.putExtra("id",item.getId());
                activity.startActivity(intent);
            }
        });

        holder.buttonNewsPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewsPaperDetails.class);
                intent.putExtra("id",item.getNewsPaperId());
                activity.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return modelPostsCardArrayList.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout cardView_container;
        AppCompatButton buttonNewsPaper;
        TextView textView_title,textView_date,textView_category;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView_container = itemView.findViewById(R.id.container);
            buttonNewsPaper = itemView.findViewById(R.id.post_card_newspaper_btn);
            textView_title = itemView.findViewById(R.id.post_card_title);
            textView_date = itemView.findViewById(R.id.date);
            textView_category = itemView.findViewById(R.id.category);
            imageView = itemView.findViewById(R.id.post_card_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}

