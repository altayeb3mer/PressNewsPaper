package com.example.pressnewspaper.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Activity.PostDetailsActivity;
import com.example.pressnewspaper.Model.ModelOtherPosts;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

import java.util.ArrayList;


public class AdapterOtherPosts extends RecyclerView.Adapter<AdapterOtherPosts.ViewHolder> {

    ArrayList<ModelOtherPosts> otherPostsArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;

    public AdapterOtherPosts(Activity activity, ArrayList<ModelOtherPosts> otherPostsArrayList) {
        this.mInflater = LayoutInflater.from(activity);
        this.otherPostsArrayList = otherPostsArrayList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.other_posts_items, parent, false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelOtherPosts item = otherPostsArrayList.get(position);

        holder.buttonNewsPaper.setText(item.getNewsPaperName());
        holder.textView_title.setText(item.getTitle());

        Glide.with(activity).load(Api.ROOT_URL + "storage/" + item.getImg_url())
                .into(holder.imageView);

        holder.cardView_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PostDetailsActivity.class);
                intent.putExtra("id", item.getPostId());
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return otherPostsArrayList.size();
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
        CardView cardView_container;
        AppCompatButton buttonNewsPaper;
        TextView textView_title;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            cardView_container = itemView.findViewById(R.id.other_post_card);
            buttonNewsPaper = itemView.findViewById(R.id.other_post_btn);
            textView_title = itemView.findViewById(R.id.other_post_title);
            imageView = itemView.findViewById(R.id.other_post_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}

