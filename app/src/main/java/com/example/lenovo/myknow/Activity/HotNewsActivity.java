package com.example.lenovo.myknow.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myknow.Bean.HotNews;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.lenovo.myknow.R.menu.toolbarhotnews;

public class HotNewsActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<HotNews> list;
    private String newsId;
    private String name;
    private String share_url;
    private String long_comments;
    private String short_comments;
    private String popularity;

    public HotNewsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);                    //得到用户名
        name = sps.getString("theName","");
        setContentView(R.layout.activity_hot_news);
        dbHelper = new MyDatabaseHelper(this,"theHotNews.db",null,1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.ToolbarHotnews);                                                      //初始化Toolbar
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
        final String Url = intent.getStringExtra("thehotnews");                                         //接收消息Id
        newsId = intent.getStringExtra("newsId");
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
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    URL url = new URL(Url);                                                         //页面地址
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
                        Log.d("Test1", response.toString());
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
        int op = 0;
        getMenuInflater().inflate(R.menu.toolbarhotnews,menu);
        MenuItem item = menu.findItem(R.id.collect);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("theHotNews", new String[]{"newsId","Owner"}, null, null, null, null,null);
        if(cursor.moveToFirst()) {
            do {
                String Owner = cursor.getString(cursor.getColumnIndex("Owner"));                     //检验该消息是否被收藏
                String id=cursor.getString(cursor.getColumnIndex("newsId"));
                if(name.equals(Owner)) {
                    if (newsId.equals(id)) {
                        op = op + 1;
                    }
                }
            }
            while(cursor.moveToNext());
        }cursor.close();
        if(op != 0){ item.setIcon(R.drawable.collect1);}                              //若收藏置换为收藏图片
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item .getItemId()){
            case R.id.collect:                        //收藏按钮
                int op1 = 0;
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
                    Intent intent = getIntent();
                    String title = intent.getStringExtra("title");
                    String ImagrUrl = intent.getStringExtra("ImageUrl");
                    ContentValues values = new ContentValues();
                    values.put("Owner", name);
                    values.put("newsTitle", title);
                    values.put("newsId", newsId);
                    values.put("ImageUrl", ImagrUrl);
                    Log.e("TAGGGG","这里是HotNewsActivity"+name + newsId+ title + ImagrUrl);
                    db.insert("theHotNews", null, values);
                    values.clear();
                }
                else {
                    db.delete("theHotNews","newsId =? and Owner = ?", new String[] {newsId,name});
                    item.setIcon(R.drawable.collect);
                }
                break;
           case R.id.good:               //点赞数
                Toast.makeText(HotNewsActivity.this,popularity,Toast.LENGTH_LONG).show();
                break;
            case R.id.remark:                 //跳转至评论页面
                Intent intent1 = new Intent(HotNewsActivity.this,RemarkActivity.class);
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
                WebView webView = (WebView)findViewById(R.id.hotNews);         //使用webView显示
                webView.loadUrl(share_url);
            }
        });
    }

    private void  parseJSONWithJSONObject(String JsonData){                                          //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            share_url = jsonObject.getString("share_url");
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
            Log.e("TAG",popularity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
