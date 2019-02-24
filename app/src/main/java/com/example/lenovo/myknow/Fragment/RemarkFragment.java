package com.example.lenovo.myknow.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.myknow.Activity.RemarkActivity;
import com.example.lenovo.myknow.Adapter.LongRemarkAdapter;
import com.example.lenovo.myknow.Bean.LongRemark;
import com.example.lenovo.myknow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemarkFragment extends Fragment {

    Context mContext;
    private String title;
    private String url;
    private List<LongRemark> longlist = new ArrayList<>();
    RecyclerView recyclerview;

    public static RemarkFragment newInstance(String title, String url) {
        RemarkFragment remarkFragment = new RemarkFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Url", url);
        remarkFragment.setArguments(bundle);
        return remarkFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            title = args.getString("Title");
            url = args.getString("Url");
        }
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {

                    URL theurl = new URL(url);                                            //页面地址
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(theurl).build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } }).start();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {  //创建视图
        View  view = inflater.inflate(R.layout.remark_fragment, container, false);                 //初始化recycleView
     //  RecyclerView recyclerView = view.findViewById(R.id.remarkFragment);
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

       // recyclerview.setLayoutManager(linearLayoutManager);
      //  LongRemarkAdapter adapter = new LongRemarkAdapter(longlist);
      //  recyclerview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    public String getTitle(){
        Bundle args = getArguments();
        title = args.getString("Title");
        return title;
    }
    private void  parseJSONWithJSONObject(String JsonData){                                            //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String content = jsonObject1.getString("content");
                String avatar = jsonObject1.getString("avatar");
                String likes = jsonObject1.getString("likes");
                LongRemark longRemark = new LongRemark();                                                     //将标题和图片加入RecycleView
                longRemark.setLongReviewImage(avatar);
                longRemark.setLongReviewer(author);
                longRemark.setLongText(content);
                longRemark.setLikeNum(likes);
                longlist.add(longRemark);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerview = (RecyclerView) getView().findViewById(R.id.remarkFragment);               //初始化recycleView
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                recyclerview.setLayoutManager(linearLayoutManager);
                LongRemarkAdapter adapter = new LongRemarkAdapter(longlist);
                recyclerview.setAdapter(adapter);

            }
        });
    }

}
