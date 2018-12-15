package com.example.lenovo.myknow.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Activity.HotNewsActivity;
import com.example.lenovo.myknow.Bean.HotNews;
import com.example.lenovo.myknow.R;

import java.util.List;

public class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.ViewHolder> {
    private List<HotNews> list;
    private Context mContext;
    private String hot;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView title;
        View hotNewsView;

        public ViewHolder(View view) {
            super(view);
            hotNewsView = view;
            title = (TextView) view.findViewById(R.id.Title);
            newsImage = (ImageView) view.findViewById(R.id.newsImage);
        }
    }

    public HotNewsAdapter(List<HotNews> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotNewsAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_hotnews, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                hot = hotNews.getURL();
                String id = hotNews.getId();
                String title = hotNews.getTitle();
                String ImageUrl = hotNews.getImageId();
                Intent intent = new Intent(view.getContext(),HotNewsActivity.class);
                intent.putExtra("thehotnews",hot);
                intent.putExtra("newsId",id);
                intent.putExtra("title",title);
                intent.putExtra("ImageUrl",ImageUrl);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                hot = hotNews.getURL();
                String id = hotNews.getId();
                String title = hotNews.getTitle();
                String ImageUrl = hotNews.getImageId();
                Intent intent = new Intent(view.getContext(),HotNewsActivity.class);
                intent.putExtra("thehotnews",hot);
                intent.putExtra("newsId",id);
                intent.putExtra("title",title);
                intent.putExtra("ImageUrl",ImageUrl);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HotNews hotNews = list.get(i);
        Glide.with(mContext).load(hotNews. getImageId()).into(viewHolder.newsImage);
        viewHolder.title.setText(hotNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
