package com.example.lenovo.myknow.Activity;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.lenovo.myknow.Bean.CollectColumn;
import com.example.lenovo.myknow.Adapter.CollectColumnAdapter;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private List<CollectColumn> mlist = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        dbHelper = new MyDatabaseHelper(this, "theColumn.db", null, 1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarCollect);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipeCollectColumn);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mlist.clear();
                refreshColumn();
                swipeRefresh.setRefreshing(false);
            }
        });

    }
    private void refreshColumn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sps = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                    String name = sps.getString("theName","");
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.query("theColumn",new String[]{"Owner","columnId","columnTitle","ImageUrl"},null,null,null,null,null);
                    if (cursor.moveToFirst()){
                        do {
                            String user = cursor.getString(cursor.getColumnIndex("Owner"));
                            String title = cursor.getString(cursor.getColumnIndex("columnTitle"));
                            String id = cursor.getString(cursor.getColumnIndex("columnId"));
                            String url = cursor.getString(cursor.getColumnIndex("Imageurl"));
                            if (name.equals(user)){
                                CollectColumn collectColumn = new CollectColumn();
                                collectColumn.setID(id);
                                collectColumn.setImage(url);
                                collectColumn.setTitle(title);
                                mlist.add(collectColumn);
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
        Cursor cursor = db.query("theColumn",new String[]{"Owner","columnId","columnTitle","ImageUrl"},null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String user = cursor.getString(cursor.getColumnIndex("Owner"));
                String title = cursor.getString(cursor.getColumnIndex("columnTitle"));
                String id = cursor.getString(cursor.getColumnIndex("columnId"));
                String url = cursor.getString(cursor.getColumnIndex("Imageurl"));
                if (name.equals(user)){
                    CollectColumn collectColumn = new CollectColumn();
                    collectColumn.setID(id);
                    collectColumn.setImage(url);
                    collectColumn.setTitle(title);
                    mlist.add(collectColumn);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcyCollectColumn);                //初始化
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CollectActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CollectColumnAdapter adapter = new CollectColumnAdapter(mlist);
        recyclerView.setAdapter(adapter);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_collect,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item .getItemId()){
            case R.id.news:
                Intent intent = new Intent(CollectActivity.this,NewsCollectActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }
}
