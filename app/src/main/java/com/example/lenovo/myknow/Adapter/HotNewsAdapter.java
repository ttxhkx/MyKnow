package com.example.lenovo.myknow.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Activity.HotNewsActivity;
import com.example.lenovo.myknow.Bean.HotNews;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.List;

public class HotNewsAdapter extends RecyclerView.Adapter<HotNewsAdapter.ViewHolder> {
    private List<HotNews> list;
    private Context mContext;
    private String hot;
    /*private MyDatabaseHelper dbHelper;
    int op;
    String name;
    String newsId;*/

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView title;
        //Button hotNewsCollect;
        View hotNewsView;

        public ViewHolder(View view) {
            super(view);
            hotNewsView = view;
            title = (TextView) view.findViewById(R.id.Title);
            newsImage = (ImageView) view.findViewById(R.id.newsImage);
            //hotNewsCollect = (Button)view.findViewById(R.id.hotNewsCollect);
        }
    }

    public HotNewsAdapter(List<HotNews> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HotNewsAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {

        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_hotnews, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        /*dbHelper = new MyDatabaseHelper(mContext,"theHotNews.db",null,1);
        SharedPreferences sps = mContext.getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name = sps.getString("theName","");

        HotNews hotNews = list.get(i);
        newsId = hotNews.getId();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("theHotNews", new String[]{"newsId","Owner"}, null, null, null, null,null);
        if(cursor.moveToFirst()) {
            do {
                String Owner = cursor.getString(cursor.getColumnIndex("Owner"));
                String id=cursor.getString(cursor.getColumnIndex("newsId"));
                if(name.equals(Owner)) {
                    if (newsId.equals(id)) {
                        op = op + 1;
                        Log.e("TAG1", String.valueOf(op));
                    }
                }
            }
            while(cursor.moveToNext());
        }cursor.close();
        Log.e("TAG2", String.valueOf(op));
        if(op!=0){
            holder.hotNewsCollect.setBackgroundResource(R.drawable.collect3);
        } else {
            holder.hotNewsCollect.setBackgroundResource(R.drawable.collect2);
        }
        Log.e("TAG3", String.valueOf(op));
        op = 0;*/
        holder.newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                HotNews hotNews = list.get(i);
                hot = hotNews.getURL();
                String id = hotNews.getId();
                String title = hotNews.getTitle();
                String ImageUrl = hotNews.getImageId();
                Intent intent = new Intent(view.getContext(), HotNewsActivity.class);
                intent.putExtra("thehotnews", hot);
                intent.putExtra("newsId", id);
                intent.putExtra("title", title);
                intent.putExtra("ImageUrl", ImageUrl);
                view.getContext().startActivities(new Intent[]{intent});
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
                Intent intent = new Intent(view.getContext(), HotNewsActivity.class);
                intent.putExtra("thehotnews", hot);
                intent.putExtra("newsId", id);
                intent.putExtra("title", title);
                intent.putExtra("ImageUrl", ImageUrl);
                view.getContext().startActivities(new Intent[]{intent});
            }
        });
        /*holder.hotNewsCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Log.e("TAG", String.valueOf(op));
                Cursor cursor = db.query("theHotNews", new String[]{"newsId","Owner"}, null, null, null, null,null);
                if(cursor.moveToFirst()) {
                    do {
                        String Owner = cursor.getString(cursor.getColumnIndex("Owner"));
                        String id=cursor.getString(cursor.getColumnIndex("newsId"));
                        if(name.equals(Owner)) {
                            if (newsId.equals(id)) {
                                op = op + 1;
                            }
                        }
                    }
                    while(cursor.moveToNext());
                }cursor.close();
                if (op == 0){
                    int i = holder.getAdapterPosition();
                    HotNews hotNews = list.get(i);
                    ContentValues values = new ContentValues();
                    values.put("Owner", name);
                    values.put("newsTitle", hotNews.getTitle());
                    values.put("newsId", newsId);
                    values.put("ImageUrl", hotNews.getImageId());
                    db.insert("theHotNews", null, values);
                    values.clear();
                    holder.hotNewsCollect.setBackgroundResource(R.drawable.collect3);
                    op = 0;
                }
                else {
                    db.delete("theHotNews","newsId =? and Owner = ?", new String[] {newsId,name});
                    holder.hotNewsCollect.setBackgroundResource(R.drawable.collect2);
                    op = 0;
                }
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HotNews hotNews = list.get(i);
        Glide.with(mContext).load(hotNews.getImageId()).into(viewHolder.newsImage);
        viewHolder.title.setText(hotNews.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
