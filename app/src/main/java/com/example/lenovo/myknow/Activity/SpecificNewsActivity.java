package com.example.lenovo.myknow.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpecificNewsActivity extends AppCompatActivity {

    private String share_url;
    private String name;
    private String newsId;
    private String popularity;
    private String long_comments;
    private String short_comments;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_news);
        dbHelper = new MyDatabaseHelper(this,"theHotNews.db",null,1);
        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name = sps.getString("theName","");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarSpecific);
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
        newsId = intent.getStringExtra("news");
        final String Url = "https://news-at.zhihu.com/api/4/news/" + newsId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String a = "https://news-at.zhihu.com/api/4/story-extra/" + newsId;
                    URL url = new URL(a);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();        //对象
                    connection.setRequestMethod("GET");                                             //设置请求方式
                    connection.setConnectTimeout(8000);                                             //设置连接超时时间
                    connection.setReadTimeout(8000);                                                //读取超时时间
                    connection.connect();                                                           //连接网络
                    int responseCode = connection.getResponseCode();                                //读取响应码
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //获取输入流
                        StringBuilder response = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        parseJSONWithJSONObject1(response.toString());
                        reader.close();
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();        //对象
                    connection.setRequestMethod("GET");                                             //设置请求方式
                    connection.setConnectTimeout(8000);                                             //设置连接超时时间
                    connection.setReadTimeout(8000);                                                //读取超时时间
                    connection.connect();                                                           //连接网络
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_specfic_news,menu);
        MenuItem item = menu.findItem(R.id.collect);
        int op = 0;
        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        name = sps.getString("theName","");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
        if(op != 0){ item.setIcon(R.drawable.collect1);}
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item .getItemId()){
            case R.id.Scollect:
                int op1 = 0;
                SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                name = sps.getString("theName","");
                Intent intent = getIntent();
                String title = intent.getStringExtra("title");
                String ImagrUrl = intent.getStringExtra("ImageUrl");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("theHotNews", new String[]{"newsId","Owner"}, null, null, null, null,null);
                if(cursor.moveToFirst()) {
                    do {
                        String Name = cursor.getString(cursor.getColumnIndex("Owner"));
                        String id=cursor.getString(cursor.getColumnIndex("newsId"));
                        if(name.equals(Name)) {
                            if (newsId.equals(id)) {
                                op1 = op1 + 1;
                            }
                        }
                    }
                    while(cursor.moveToNext());
                }cursor.close();
                if(op1 == 0) {
                    item.setIcon(R.drawable.collect1);
                    ContentValues values = new ContentValues();
                    values.put("Owner", name);
                    values.put("newsTitle", title);
                    values.put("newsId", newsId);
                    values.put("ImageUrl", ImagrUrl);
                    db.insert("theHotNews", null, values);
                    values.clear();
                }
                else {
                    db.delete("theHotNews","newsId =? and Owner = ?", new String[] {newsId,name});
                    item.setIcon(R.drawable.collect);
                }
                break;
            case R.id.Sgood:
                Toast.makeText(SpecificNewsActivity.this,popularity,Toast.LENGTH_LONG).show();
                break;
            case R.id.Sremark:
                Intent intent1 = new Intent(SpecificNewsActivity.this,RemarkActivity.class);
                intent1.putExtra("NewsId",newsId);
                intent1.putExtra("long",long_comments);
                intent1.putExtra("short",short_comments);
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }

    private void showResponse(){                                               //转到主线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebView webView = (WebView)findViewById(R.id.specificNews);         //使用webView显示
                webView.loadUrl(share_url);
            }
        });
    }
    private void  parseJSONWithJSONObject(String JsonData){                                          //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            share_url = jsonObject.getString("share_url");
            Log.e("TAG",share_url);
            showResponse();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
    private void  parseJSONWithJSONObject1(String JsonData){                                          //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            long_comments = jsonObject.getString("long_comments");
            popularity = jsonObject.getString("popularity");
            short_comments = jsonObject.getString("short_comments");
            //short_comments = jsonObject.getString("short_comments");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
