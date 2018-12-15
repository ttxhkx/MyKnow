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
import com.example.lenovo.myknow.Activity.NewsActivity;
import com.example.lenovo.myknow.R;
import com.example.lenovo.myknow.Bean.theColumn;

import java.util.List;

public class theColumnAdapter extends RecyclerView.Adapter<theColumnAdapter.ViewHolder> {

    private List<theColumn> mlist;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ColumnImage;
        TextView ColumnID;
        TextView ColumnDescription;
        View ColumnView;

        public ViewHolder(View view) {
            super(view);
            ColumnView = view;
            ColumnID = (TextView) view.findViewById(R.id.CTitle);
            ColumnDescription = (TextView)view.findViewById(R.id.description);
            ColumnImage = (ImageView) view.findViewById(R.id.columnImage);
        }
    }
    public theColumnAdapter (List<theColumn> list) {
        this.mlist = list;
    }
    @NonNull
    @Override
    public theColumnAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_column, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.ColumnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = holder.getAdapterPosition();
                theColumn thecolumn = mlist.get(a);
                String column_id = thecolumn.getID();
                String Image = thecolumn.getImage();
                String title = thecolumn.getTitle();
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                intent.putExtra("Id",column_id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.ColumnID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = holder.getAdapterPosition();
                theColumn thecolumn = mlist.get(a);
                String column_id = thecolumn.getID();
                String Image = thecolumn.getImage();
                String title = thecolumn.getTitle();
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                intent.putExtra("Id",column_id);
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        holder.ColumnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = holder.getAdapterPosition();
                theColumn thecolumn = mlist.get(a);
                String column_id = thecolumn.getID();
                String Image = thecolumn.getImage();
                String title = thecolumn.getTitle();
                Intent intent = new Intent(view.getContext(),NewsActivity.class);
                intent.putExtra("Id",column_id);;
                intent.putExtra("ImageUrl",Image);
                intent.putExtra("columnTitle",title);
                view.getContext().startActivities(new Intent[] {intent});
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull theColumnAdapter.ViewHolder viewHolder, int i) {
        theColumn thecolumn = mlist.get(i);
        Glide.with(mContext).load(thecolumn. getImage()).into(viewHolder.ColumnImage);
        viewHolder.ColumnID.setText(thecolumn.getTitle());
        viewHolder.ColumnDescription.setText(thecolumn.getDescription());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
