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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.myknow.Bean.LongRemark;
import com.example.lenovo.myknow.R;

import java.util.List;

public class LongRemarkAdapter extends RecyclerView.Adapter<LongRemarkAdapter.ViewHolder> {

    private List<LongRemark> mlist;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {                                //修改后为长评和短评共同的Adapter
        ImageView LongReviewImage;
        TextView LongReviewer;
        TextView LongText;
        TextView LikeNum;
        View LongRemarkView;

        public ViewHolder(View view) {
            super(view);
            LongRemarkView = view;
            LongReviewer = (TextView)view.findViewById(R.id.LongReviewer);
            LongReviewImage = (ImageView)view.findViewById(R.id.LongReviewImage);
            LongText = (TextView)view.findViewById(R.id.LongText);
            LikeNum = (TextView)view.findViewById(R.id.LikeNum);

        }
    }
    public LongRemarkAdapter(List<LongRemark> list){this.mlist = list;}

    @NonNull
    @Override
    public LongRemarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_long_remark,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LongRemarkAdapter.ViewHolder viewHolder, int i) {
        LongRemark longRemark = mlist.get(i);
        Glide.with(mContext).load(longRemark. getLongReviewImage()).into(viewHolder.LongReviewImage);
        viewHolder.LongText.setText(longRemark.getLongText());
        viewHolder.LongReviewer.setText(longRemark.getLongReviewer());
        viewHolder.LikeNum.setText(longRemark.getLikeNum());
        Log.e("TAG","传过来了long");
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
