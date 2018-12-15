package com.example.lenovo.myknow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_USERS = "create table Users ("
            + "id text, "
            + "NickName text,"
            + "Code text,"
            + "ImageId blob,"
            + "Phone text)";
    private static final String CREATE_THECOLUMN = "create table theColumn("
            + "id integer primary key autoincrement,"
            + "Owner text,"
            + "columnId text,"
            + "Imageurl text,"
            + "columnTitle text)";
    private static final String CREATE_THEHOTNEWS = "create table theHotNews("
            + "id integer primary key autoincrement,"
            + "Owner text,"
            + "newsId text,"
            + "ImageUrl text,"
            + "newsTitle text)";

    private Context mcontext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_THECOLUMN);
        db.execSQL(CREATE_THEHOTNEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Users");
        db.execSQL("drop table if exists theColumn");
        db.execSQL("drop table if exists theHotNews");
        onCreate(db);
    }
}
