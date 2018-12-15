package com.example.lenovo.myknow.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.myknow.Adapter.ShortRemarkAdapter;
import com.example.lenovo.myknow.Bean.ShortRemark;
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

public class ShortRemarkActivity extends AppCompatActivity {
    private List<ShortRemark> shortlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_remark);
        Intent intent = getIntent();
        String srkNum = intent.getStringExtra("short");
        final String shortUrl = intent.getStringExtra("shortUrl");
        TextView srk = (TextView)findViewById(R.id.srk);
        String shortNum ="短评："+ srkNum;
        srk.setText(shortNum);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {

                    URL url = new URL(shortUrl);            //页面地址
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();        //对象
                    connection.setRequestMethod("GET");                                             //设置请求方式
                    connection.connect();                                                           //连接网络
                    connection.setConnectTimeout(8000);                                             //设置连接超时时间
                    connection.setReadTimeout(8000);                                                //读取超时时间
                    int responseCode = connection.getResponseCode();                                //读取响应码
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //获取输入流
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        parseJSONWithJSONObject(String.valueOf(response));
                        reader.close();
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } }).start();
    }
    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.shortRemark);               //初始化recycleView
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShortRemarkActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                ShortRemarkAdapter adapter = new ShortRemarkAdapter(shortlist);
                recyclerView.setAdapter(adapter);

            }
        });
    }
    private void  parseJSONWithJSONObject(String JsonData){                                         //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String content = jsonObject1.getString("content");
                String avatar = jsonObject1.getString("avatar");
                String likes = jsonObject1.getString("likes");
                Log.e("TAG",avatar);
                Log.e("TAG",author);
                ShortRemark shortRemark = new ShortRemark();                                                     //将标题和图片加入RecycleView
                shortRemark.setShortReviewImage(avatar);
                shortRemark.setShortReviewer(author);
                shortRemark.setShortText(content);
                shortRemark.setSlikeNum(likes);
                shortlist.add(shortRemark);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
