package com.example.lenovo.myknow.Activity;

import android.content.Context;
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
import android.view.View;

import com.example.lenovo.myknow.Adapter.CollectColumnAdapter;
import com.example.lenovo.myknow.Adapter.CollectNewsAdapter;
import com.example.lenovo.myknow.Bean.CollectNews;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class NewsCollectActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<CollectNews> mlist = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_collect);
        dbHelper = new MyDatabaseHelper(this, "theHotNews.db", null, 1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarNewsCollect);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeCollectNews);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist.clear();
                refreshNews();
                swipeRefresh.setRefreshing(false);
            }
        });
    }
    private void refreshNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                    String name = sps.getString("theName","");
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("theHotNews",new String[]{"Owner","newsId","newsTitle","ImageUrl"},null,null,null,null,null);
                    if (cursor.moveToFirst()){
                        do {
                            String user = cursor.getString(cursor.getColumnIndex("Owner"));
                            String title = cursor.getString(cursor.getColumnIndex("newsTitle"));
                            String id = cursor.getString(cursor.getColumnIndex("newsId"));
                            String url = cursor.getString(cursor.getColumnIndex("ImageUrl"));
                            if (name.equals(user)){
                                CollectNews collectNews = new CollectNews();
                                collectNews.setID(id);
                                collectNews.setImage(url);
                                collectNews.setTitle(title);
                                mlist.add(collectNews);
                            }
                        }while (cursor.moveToNext());
                    }
                    cursor.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mlist.clear();
        SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        String name = sps.getString("theName","");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("theHotNews",new String[]{"Owner","newsId","newsTitle","ImageUrl"},null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String user = cursor.getString(cursor.getColumnIndex("Owner"));
                String title = cursor.getString(cursor.getColumnIndex("newsTitle"));
                String id = cursor.getString(cursor.getColumnIndex("newsId"));
                String url = cursor.getString(cursor.getColumnIndex("ImageUrl"));
                if (name.equals(user)){
                    CollectNews collectNews = new CollectNews();
                    collectNews.setID(id);
                    collectNews.setImage(url);
                    collectNews.setTitle(title);
                    mlist.add(collectNews);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcyCollectNews);                //初始化
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsCollectActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CollectNewsAdapter adapter = new CollectNewsAdapter(mlist);
        recyclerView.setAdapter(adapter);
    }
}
