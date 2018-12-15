package com.example.lenovo.myknow.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Bean.ShortRemark;
import com.example.lenovo.myknow.R;

import java.util.List;

public class ShortRemarkAdapter extends RecyclerView.Adapter<ShortRemarkAdapter.ViewHolder> {
    private List<ShortRemark> mlist;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ShortReviewImage;
        TextView ShortReviewer;
        TextView ShortText;
        TextView SlikeNum;
        View ShortRemarkView;

        public ViewHolder(View view) {
            super(view);
            ShortRemarkView = view;
            ShortReviewer = (TextView)view.findViewById(R.id.ShortReviewer);
            ShortReviewImage = (ImageView)view.findViewById(R.id.ShortReviewImage);
            ShortText = (TextView)view.findViewById(R.id.ShortText);
            SlikeNum = (TextView)view.findViewById(R.id.SlikeNum);

        }
    }
    public ShortRemarkAdapter(List<ShortRemark>list){this.mlist = list;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_short_remark,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ShortRemark shortRemark = mlist.get(i);
        Glide.with(mContext).load(shortRemark. getShortReviewImage()).into(viewHolder.ShortReviewImage);
        viewHolder.ShortText.setText(shortRemark.getShortText());
        viewHolder.ShortReviewer.setText(shortRemark.getShortReviewer());
        viewHolder.SlikeNum.setText(shortRemark.getSlikeNum());
        Log.e("TAG","传过来了short");
    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
