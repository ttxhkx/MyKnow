package com.example.lenovo.myknow.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.myknow.Adapter.CollectNewsAdapter;
import com.example.lenovo.myknow.Adapter.NewsAdapter;
import com.example.lenovo.myknow.Bean.CollectNews;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private MyDatabaseHelper dbHelper;
    private List<CollectNews> mlist = new ArrayList<>();
    Context mContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        return view;
    }

    @Override
    public void onResume() {
        mlist.clear();
        SharedPreferences sps = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        String name = sps.getString("theName", "");
        dbHelper = new MyDatabaseHelper(mContent, "theHotNews.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("theHotNews", new String[]{"Owner", "newsId", "newsTitle", "ImageUrl"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String user = cursor.getString(cursor.getColumnIndex("Owner"));
                String title = cursor.getString(cursor.getColumnIndex("newsTitle"));
                String id = cursor.getString(cursor.getColumnIndex("newsId"));
                String url = cursor.getString(cursor.getColumnIndex("ImageUrl"));
                if (name.equals(user)) {
                    CollectNews collectNews = new CollectNews();
                    collectNews.setID(id);
                    collectNews.setImage(url);
                    collectNews.setTitle(title);
                    mlist.add(collectNews);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.newsFragment); //初始化
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContent);
        recyclerView.setLayoutManager(linearLayoutManager);
        CollectNewsAdapter adapter = new CollectNewsAdapter(mlist);
        recyclerView.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
