package com.example.lenovo.myknow.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lenovo.myknow.Bean.LongRemark;
import com.example.lenovo.myknow.Adapter.LongRemarkAdapter;
import com.example.lenovo.myknow.R;
import com.example.lenovo.myknow.Bean.ShortRemark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RemarkActivity extends AppCompatActivity {
    private List<LongRemark> longlist = new ArrayList<>();
    private String ShortUrl;
    private String srkNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        Intent intent = getIntent();
        String id = intent.getStringExtra("NewsId");
        String lrkNum = intent.getStringExtra("long");
        srkNum = intent.getStringExtra("short");
        TextView lrk = (TextView)findViewById(R.id.lrk);
        String longNum = "长评" + lrkNum;
        lrk.setText(longNum);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarRemark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String LongUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/long-comments";
        ShortUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/short-comments";
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {

                    URL url = new URL(LongUrl);            //页面地址
                    Log.e("TAG","至少这里可行");
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_remark,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item .getItemId()){
            case R.id.remark:
                Intent intent = new Intent(RemarkActivity.this,ShortRemarkActivity.class);
                intent.putExtra("short",srkNum);
                intent.putExtra("shortUrl",ShortUrl);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }
    private void showResponse(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerview = (RecyclerView) findViewById(R.id.longRemark);               //初始化recycleView
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RemarkActivity.this);
                recyclerview.setLayoutManager(linearLayoutManager);
                LongRemarkAdapter adapter = new LongRemarkAdapter(longlist);
                recyclerview.setAdapter(adapter);

            }
        });
    }
    private void  parseJSONWithJSONObject(String JsonData){                                            //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String content = jsonObject1.getString("content");
                String avatar = jsonObject1.getString("avatar");
                String likes = jsonObject1.getString("likes");
                Log.e("TAG",content);
                Log.e("TAG",likes);
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
}
