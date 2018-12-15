package com.example.lenovo.myknow.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myknow.Adapter.theColumnAdapter;
import com.example.lenovo.myknow.Bean.HotNews;
import com.example.lenovo.myknow.Adapter.HotNewsAdapter;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class ZhiHuActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private DrawerLayout mDrawerLayout;
    String NickName;
    private MyDatabaseHelper dbHelper;
    private MyDatabaseHelper dbHelper2;
    private MyDatabaseHelper dbHelper3;
    private List<HotNews> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_hu);

        dbHelper = new MyDatabaseHelper(this, "Users.db", null, 1);
        dbHelper2 = new MyDatabaseHelper(this, "theColumn.db", null, 1);
        dbHelper3 = new MyDatabaseHelper(this, "theHotNews.db", null, 1);
        setContentView(R.layout.activity_zhi_hu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);                          //初始化Toobar.
        setSupportActionBar(toolbar);
        toolbar.setTitle("知乎日报");
        Intent intent = getIntent();                                                           //接收用户名
        final String username = intent.getStringExtra("ID");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {                                                               //加导航图片
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.daohang2);
        }
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeHotnews);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHotNews();
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.user_drawer_layout);                  //初始化抽屉式布局
        NavigationView user_inf = (NavigationView) findViewById(R.id.user_inf);
        user_inf.inflateHeaderView(R.layout.user_header);
        user_inf.inflateMenu(R.menu.user_item);

        user_inf.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {                              //绑定抽屉上的按钮
                int id = menuItem.getItemId();
                if (id == R.id.column) {
                    Intent intent = new Intent(ZhiHuActivity.this, theColumnActivity.class);
                    startActivity(intent);
                }
                if (id == R.id.MyCollection) {
                    Intent intent1 = new Intent(ZhiHuActivity.this, CollectActivity.class);
                    startActivity(intent1);
                }
                if (id == R.id.Revise) {
                    Intent intent = new Intent(ZhiHuActivity.this, informationActivity.class);
                    intent.putExtra("NAME", username);
                    startActivity(intent);
                }
                if (id == R.id.cancel) {                                                              //删除三张表中所有数据
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    SQLiteDatabase db2 = dbHelper2.getWritableDatabase();
                    SQLiteDatabase db3 = dbHelper3.getWritableDatabase();
                    db.delete("Users", "id=?", new String[]{username});
                    db2.delete("theColumn", "Owner=?", new String[]{username});
                    db3.delete("theHotNews", "Owner=?", new String[]{username});
                    Toast.makeText(ZhiHuActivity.this, "注销完成", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ZhiHuActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (id == R.id.exit) {
                    finish();
                }
                return true;
            }
        });
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/4/news/hot");            //页面地址
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
                        parseJSONWithJSONObject(String.valueOf(response));
                        reader.close();
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void refreshHotNews() {
        new Thread(new Runnable() {                                                                 //新线程联网
            @Override
            public void run() {
                try {
                    URL url = new URL("https://news-at.zhihu.com/api/4/news/hot");            //页面地址
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
                        showResponse();
                        reader.close();
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {                                                                     //重写以修改头像和昵称后更新
        super.onResume();
        Intent intent = getIntent();
        String username = intent.getStringExtra("ID");
        NavigationView user_inf = (NavigationView) findViewById(R.id.user_inf);
        View drawView = user_inf.getHeaderView(0);
        ImageView headImage = (ImageView) drawView.findViewById(R.id.userImage);
        dbHelper = new MyDatabaseHelper(this, "Users.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Users", new String[]{"ImageId","id"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("id"));
                byte[] in = cursor.getBlob(cursor.getColumnIndex("ImageId"));
                if(name.equals(username)){
                    Bitmap bitmap = getBitmapFromByte(in);
                    headImage.setImageBitmap(bitmap);
                }
            } while (cursor.moveToNext()) ;
         }
        cursor.close();
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                                      //头像监听。
                Intent intent = new Intent(ZhiHuActivity.this,ChangeHeadimageActivity.class);
                startActivity(intent);
            }
        });
        final TextView name = (TextView)drawView.findViewById(R.id.userName);                                    //显示昵称
        Cursor cursor1;
        cursor1 = db.query("Users",new String[]{"id","NickName"},null,null,null,null,null);
        if (cursor1.moveToFirst()) {
            do {
                String Name = cursor1.getString(cursor1.getColumnIndex("id"));
                if (Name.equals(username)) {
                    NickName = cursor1.getString(cursor1.getColumnIndex("NickName"));
                    break;
                }
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        name.setText(NickName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){                                            //导航栏逻辑
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    private void showResponse(){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);               //初始化recycleView
               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ZhiHuActivity.this);
               recyclerView.setLayoutManager(linearLayoutManager);
               HotNewsAdapter adapter = new HotNewsAdapter(list);
               recyclerView.setAdapter(adapter);
               recyclerView.removeAllViews();
               adapter.notifyDataSetChanged();
               swipeRefresh.setRefreshing(false);
           }
       });
    }
    private void  parseJSONWithJSONObject(String JsonData){                                         //解析JSON数据
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("recent");
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String news_id = jsonObject1.getString("news_id");
                String title = jsonObject1.getString("title");
                String thumbnail = jsonObject1.getString("thumbnail");
                String url = jsonObject1.getString("url");
                HotNews hotnews = new HotNews();                                                     //将标题和图片加入RecycleView
                hotnews.setImageId(thumbnail);
                hotnews.setURL(url);
                hotnews.setId(news_id);
                hotnews.setTitle(title);
                list.add(hotnews);
            }
            showResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public Bitmap getBitmapFromByte(byte[] temp){
        if(temp != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        }else{
            return null;
        }
    }
}
