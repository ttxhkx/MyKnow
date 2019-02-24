package com.example.lenovo.myknow.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.lenovo.myknow.Adapter.HotNewsAdapter;
import com.example.lenovo.myknow.Adapter.theColumnAdapter;
import com.example.lenovo.myknow.Bean.HotNews;
import com.example.lenovo.myknow.Bean.theColumn;
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

public class theColumnActivity extends AppCompatActivity {

    private List<theColumn> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private String name;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_column);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarColumn);
        toolbar.setTitle("栏目总览");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name = sps.getString("theName","");

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeColumn);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshColumns();
            }
        });
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/4/sections");            //页面地址
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
                        parseJSONWithJSONObject(response.toString());
                        reader.close();
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } }).start();
    }
    private void  parseJSONWithJSONObject(final String JsonData){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerViewColumn);           //初始化recycleView

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(theColumnActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    list.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(JsonData);                                        //解析JSON数据
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String title = jsonObject1.getString("name");
                            String thumbnail = jsonObject1.getString("thumbnail");
                            String description = jsonObject1.getString("description");
                            theColumn thecolumn = new theColumn();                                                     //将标题和图片加入RecycleView
                            thecolumn.setImage(thumbnail);
                            thecolumn.setID(id);
                            thecolumn.setTitle(title);
                            thecolumn.setDescription(description);
                            list.add(thecolumn);
                        }
                        theColumnAdapter adapter = new theColumnAdapter(list);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                        swipeRefresh.setRefreshing(false);
                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
    });
    }
    private void refreshColumns(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/4/sections");            //页面地址
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();        //对象
                    connection.setRequestMethod("GET");                                             //设置请求方式
                    connection.connect();                                                           //连接网络
                    connection.setConnectTimeout(8000);                                             //设置连接超时时间
                    connection.setReadTimeout(8000);                                                //读取超时时间
                    int responseCode = connection.getResponseCode();                                //读取响应码
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //获取输入流
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        parseJSONWithJSONObject(response.toString());
                        reader.close();
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
