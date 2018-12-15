package com.example.lenovo.myknow.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.example.lenovo.myknow.Adapter.NewsAdapter;
import com.example.lenovo.myknow.Bean.News;
import com.example.lenovo.myknow.MyDatabaseHelper;
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

public class NewsActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private String name;
    private String columnId;
    private SwipeRefreshLayout swipeRefresh;
    private List<News> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        dbHelper = new MyDatabaseHelper(this, "theColumn.db", null, 1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        columnId = intent.getStringExtra("Id");
        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name = sps.getString("theName","");
        final String Url = "https://news-at.zhihu.com/api/4/section/" + columnId;
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeNews);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {                                                                 //新线程联网
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(Url);            //页面地址
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
        });
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    URL url = new URL(Url);            //页面地址
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbarnews,menu);
        MenuItem item = menu.findItem(R.id.collect);
        int op = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("theColumn", new String[]{"columnId","Owner"}, null, null, null, null,null);
        if(cursor.moveToFirst()) {
            do {
                String Owner = cursor.getString(cursor.getColumnIndex("Owner"));
                String columnid  = cursor.getString(cursor.getColumnIndex("columnId"));
                if(name.equals(Owner)) {
                    if (columnId.equals(columnid)) {
                        op = op + 1;
                    }
                }
            }
            while(cursor.moveToNext());
        }cursor.close();
        if(op != 0){ item.setIcon(R.drawable.collect1);}
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item .getItemId()){
            case R.id.like:
                int op1 = 0;
                Intent intent = getIntent();
                String id = intent.getStringExtra("Id");
                String title = intent.getStringExtra("columnTitle");
                String Image = intent.getStringExtra("ImageUrl");

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("theColumn", new String[]{"columnId","Owner"}, null, null, null, null,null);
                if(cursor.moveToFirst()) {
                    do {
                        String Name = cursor.getString(cursor.getColumnIndex("Owner"));
                        String theID =cursor.getString(cursor.getColumnIndex("columnId"));
                        if(name.equals(Name)) {
                            if (columnId.equals(theID)) {
                                op1 = op1 + 1;
                            }
                        }
                    }
                    while(cursor.moveToNext());
                }cursor.close();
                if(op1==0) {
                    item.setIcon(R.drawable.collect1);
                    ContentValues values = new ContentValues();
                    values.put("Owner", name);
                    values.put("columnId", id);
                    values.put("ImageUrl", Image);
                    values.put("columnTitle", title);
                    db.insert("theColumn", null, values);
                    values.clear();
                }
                else {
                    db.delete("theColumn","columnId =? and Owner = ?", new String[] {columnId,name});
                    item.setIcon(R.drawable.collect);
                }
                break;
            default:
        }
        return true;
    }
    private void showResponse(){                                               //转到主线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNews);           //初始化recycleView
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                NewsAdapter adapter = new NewsAdapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });
    }
    private void  parseJSONWithJSONObject(String JsonData){                                         //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String id = jsonObject1.getString("id");
                JSONArray jsonArray1 = jsonObject1.getJSONArray("images");
                for(int j = 0;j< jsonArray1.length();j++){
                    String image = jsonArray1.getString(0);
                    News theNews = new News();
                    theNews.setNewsID(id);
                    theNews.setNewsTitle(title);
                    theNews.setNewsImage(image);
                    list.add(theNews);
                }
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
