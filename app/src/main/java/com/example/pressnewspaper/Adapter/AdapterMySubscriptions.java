package com.example.pressnewspaper.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pressnewspaper.Activity.PostDetailsActivity;
import com.example.pressnewspaper.Model.ModelMySub;
import com.example.pressnewspaper.Model.ModelPostsCard;
import com.example.pressnewspaper.R;

import java.util.ArrayList;


public class AdapterMySubscriptions extends RecyclerView.Adapter<AdapterMySubscriptions.ViewHolder> {

    ArrayList<ModelMySub> mySubArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    public AdapterMySubscriptions(Activity activity, ArrayList<ModelMySub> mySubArrayList) {
        this.mInflater = LayoutInflater.from(activity);
        this.mySubArrayList = mySubArrayList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_subs_items, parent, false);



        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelMySub item = mySubArrayList.get(position);

        holder.textView_title.setText(item.getNewsPaperName());
        holder.textView_type.setText(item.getSubscriptionsType());
        holder.textView_start_date.setText(item.getSubscriptionsStartDate());
        holder.textView_end_date.setText(item.getSubscriptionsEndDate());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, ""+item.getNewsPaperName(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return mySubArrayList.size();
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
        RelativeLayout container;
        AppCompatButton buttonEdit;
        TextView textView_title,textView_type,textView_start_date,textView_end_date;

        ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container1);
            buttonEdit = itemView.findViewById(R.id.my_sub_item_btn_edit);
            textView_title = itemView.findViewById(R.id.my_sub_item_news_paper_name);
            textView_type = itemView.findViewById(R.id.my_sub_item_type);
            textView_start_date = itemView.findViewById(R.id.my_sub_item_start_date);
            textView_end_date = itemView.findViewById(R.id.my_sub_item_end_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}

