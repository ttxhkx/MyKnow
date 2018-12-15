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

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Bean.News;
import com.example.lenovo.myknow.R;
import com.example.lenovo.myknow.Activity.SpecificNewsActivity;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News>mlist;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        View NewsView;

        public ViewHolder(View view) {
            super(view);
            NewsView = view;
            newsTitle = (TextView)view.findViewById(R.id.NewsTitle);
            newsImage = (ImageView)view.findViewById(R.id.NewsImage);
        }
    }

    public NewsAdapter(List<News>list){this.mlist = list;}

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                News theNews = mlist.get(i);
                String id = theNews.getNewsID();
                String Image = theNews.getNewsImage();
                String title = theNews.getNewsTitle();
                Intent intent = new Intent(view.getContext(),SpecificNewsActivity.class);
                intent.putExtra("news",id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("title",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                News theNews = mlist.get(i);
                String id = theNews.getNewsID();
                String Image = theNews.getNewsImage();
                String title = theNews.getNewsTitle();
                Intent intent = new Intent(view.getContext(),SpecificNewsActivity.class);
                intent.putExtra("news",id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("title",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder viewHolder, int i) {
        News theNews = mlist.get(i);
        Glide.with(mContext).load(theNews. getNewsImage()).into(viewHolder.newsImage);
        viewHolder.newsTitle.setText(theNews.getNewsTitle());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
