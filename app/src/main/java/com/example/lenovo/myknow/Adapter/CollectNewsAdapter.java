package com.example.lenovo.myknow.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Activity.SpecificNewsActivity;
import com.example.lenovo.myknow.Bean.CollectNews;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.List;

public class CollectNewsAdapter extends RecyclerView.Adapter<CollectNewsAdapter.ViewHolder> {
    private List<CollectNews> mlist;
    private Context mContext;
    private MyDatabaseHelper dbHelper;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView NewsImage;
        TextView NewsTitle;
        ImageButton delete;
        View CollectNewsView;

        public ViewHolder(@NonNull View view) {
            super(view);
            CollectNewsView = view;
            NewsImage = (ImageView)view.findViewById(R.id.collectImage);
            NewsTitle = (TextView)view.findViewById(R.id.collectTitle);
            delete = (ImageButton)view.findViewById(R.id.delete);
        }
    }
    public CollectNewsAdapter(List<CollectNews> list) {
        this.mlist = list;
    }
    @NonNull
    @Override
    public CollectNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_collect, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.NewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                CollectNews collectNews = mlist.get(i);
                String id = collectNews.getID();
                String Image = collectNews.getImage();
                String title = collectNews.getTitle();
                Intent intent = new Intent(view.getContext(),SpecificNewsActivity.class);
                intent.putExtra("news",id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.NewsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                CollectNews collectNews = mlist.get(i);
                String id = collectNews.getID();
                String Image = collectNews.getImage();
                String title = collectNews.getTitle();
                Intent intent = new Intent(view.getContext(),SpecificNewsActivity.class);
                intent.putExtra("Id",id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new MyDatabaseHelper(mContext, "theHotNews.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int i = holder.getAdapterPosition();
                CollectNews collectNews = mlist.get(i);
                String id = collectNews.getID();
                db.delete("theHotNews","newsId = ?", new String[] {id});
                mlist.remove(i);
                notifyItemRemoved(i);
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectNewsAdapter.ViewHolder viewHolder, int i) {
        CollectNews collectNews = mlist.get(i);
        Glide.with(mContext).load(collectNews.getImage()).into(viewHolder.NewsImage);
        viewHolder.NewsTitle.setText(collectNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
