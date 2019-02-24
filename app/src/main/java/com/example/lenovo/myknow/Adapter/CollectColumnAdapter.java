package com.example.lenovo.myknow.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Activity.NewsActivity;
import com.example.lenovo.myknow.Bean.CollectColumn;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.List;

public class CollectColumnAdapter extends RecyclerView.Adapter<CollectColumnAdapter.ViewHolder> {
    private List<CollectColumn> mlist;
    private Context mContext;
    private MyDatabaseHelper dbHelper;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ColumnImage;
        TextView ColumnTitle;
        ImageButton delete;
        View CollectColumnView;

        public ViewHolder(View view) {
            super(view);
            CollectColumnView = view;
            ColumnImage = (ImageView)view.findViewById(R.id.collectImage);
            ColumnTitle = (TextView)view.findViewById(R.id.collectTitle);
            delete = (ImageButton)view.findViewById(R.id.delete);
        }
    }
    public CollectColumnAdapter(List<CollectColumn> list) {
        this.mlist = list;
    }
    @NonNull
    @Override
    public CollectColumnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collect, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.ColumnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                          //跳转并传值
                int i = holder.getAdapterPosition();
                CollectColumn collectColumn = mlist.get(i);
                String column_id = collectColumn.getID();
                String Image = collectColumn.getImage();
                String title = collectColumn.getTitle();
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                intent.putExtra("Id",column_id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.ColumnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = holder.getAdapterPosition();
                CollectColumn collectColumn = mlist.get(i);
                String column_id = collectColumn.getID();
                String Image = collectColumn.getImage();
                String title = collectColumn.getTitle();
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                intent.putExtra("Id",column_id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                            //从数据库中删除
                dbHelper = new MyDatabaseHelper(mContext, "theColumn.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int i = holder.getAdapterPosition();
                CollectColumn collectColumn = mlist.get(i);
                String column_id = collectColumn.getID();
                db.delete("theColumn","columnId = ?", new String[] {column_id});
                mlist.remove(i);
                notifyItemRemoved(i);
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectColumnAdapter.ViewHolder viewHolder, int i) {
        CollectColumn collectColumn = mlist.get(i);
        Glide.with(mContext).load(collectColumn.getImage()).into(viewHolder.ColumnImage);                //显示图片
        viewHolder.ColumnTitle.setText(collectColumn.getTitle());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
