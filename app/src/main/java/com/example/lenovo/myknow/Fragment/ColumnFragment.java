package com.example.lenovo.myknow.Fragment;

import android.content.ContentUris;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.myknow.Adapter.CollectColumnAdapter;
import com.example.lenovo.myknow.Bean.CollectColumn;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.ArrayList;
import java.util.List;

public class ColumnFragment extends Fragment {
    private MyDatabaseHelper dbHelper;
    private List<CollectColumn> mlist = new ArrayList<>();
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.column_fragment, container, false);
        return view;
    }


    @Override
    public void onResume() {
        mlist.clear();
        SharedPreferences sps = getActivity().getSharedPreferences("theUser", Context.MODE_PRIVATE);
        String name = sps.getString("theName", "");
        dbHelper = new MyDatabaseHelper(mContext, "theColumn.db", null, 1);
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

        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.columnFragment);                            //初始化
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        CollectColumnAdapter adapter = new CollectColumnAdapter(mlist);
        recyclerView.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
