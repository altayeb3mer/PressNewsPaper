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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pressnewspaper.Activity.DeptPostsActivity;
import com.example.pressnewspaper.Activity.NewsPaperDetails;
import com.example.pressnewspaper.Activity.PaperPostsActivity;
import com.example.pressnewspaper.Model.ModelMySub;
import com.example.pressnewspaper.Model.ModelNewsPaper;
import com.example.pressnewspaper.R;
import com.example.pressnewspaper.Utils.Api;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterAllNewsPaper extends RecyclerView.Adapter<AdapterAllNewsPaper.ViewHolder> {

    ArrayList<ModelNewsPaper> newsPaperArrayList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;
    public AdapterAllNewsPaper(Activity activity, ArrayList<ModelNewsPaper> newsPaperArrayList) {
        this.mInflater = LayoutInflater.from(activity);
        this.newsPaperArrayList = newsPaperArrayList;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_paper_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelNewsPaper item = newsPaperArrayList.get(position);

        holder.textView_title.setText(item.getNewPaperName());
        holder.textView_type.setText(item.getNewPaperType());
        holder.textView_release_type.setText(item.getReleaseType());

        if (!item.getReleaseTime().equals("")&&!item.getReleaseTime().equals("null")){
           holder.textView_release_time.setVisibility(View.VISIBLE);
            holder.textView_release_time.setText("تصدر في"+" "+item.getReleaseTime());
        }

        try {
            Glide.with(activity).load(Api.ROOT_URL+"storage/"+item.getImg()).into(holder.imageView);
        }catch (Exception e){
            holder.imageView.setBackgroundResource(R.drawable.ic_newspaper);
        }


        holder.textView_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DeptPostsActivity.class);
                intent.putExtra("title","صحيفة"+" "+item.getNewPaperName());
                intent.putExtra("category","");
                intent.putExtra("paperId",item.getNewPaperId());
                activity.startActivity(intent);
            }
        });
        switch (item.getSubscription_status()){
            case "0":{
                holder.buttonSubscription.setText("اشتراك");
                break;
            }
            case "1":{
                holder.buttonSubscription.setText("في انتظار الموافقة");
                holder.buttonSubscription.setEnabled(false);
                break;
            }
            case "2":{
                holder.buttonSubscription.setText("تم الاشتراك");
                holder.buttonSubscription.setEnabled(false);
                break;
            }
        }
        holder.buttonSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(activity, "جارى العمل على الاشتراك..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, NewsPaperDetails.class);
                intent.putExtra("id",item.getNewPaperId());
                activity.startActivity(intent);
            }
        });
//        holder.textView_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                activity.startActivity(new Intent(activity, PaperPostsActivity.class));
//            }
//        });




    }


    @Override
    public int getItemCount() {
        return newsPaperArrayList.size();
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
        AppCompatButton buttonSubscription;
        TextView textView_title,textView_type,textView_release_type,textView_release_time;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            container = itemView.findViewById(R.id.container1);
            buttonSubscription = itemView.findViewById(R.id.btn_sub);
            textView_title = itemView.findViewById(R.id.news_item_name);
            textView_type = itemView.findViewById(R.id.news_item_type);
            textView_release_type = itemView.findViewById(R.id.news_item_release_type);
            textView_release_time = itemView.findViewById(R.id.news_item_release_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}

