package com.example.lenovo.myknow.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

public class LoginActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new MyDatabaseHelper(this,"Users.db",null,1);
        Button BtnLogin = (Button)findViewById(R.id.BtnLogin);
        Button BtnRegister = (Button)findViewById(R.id.BtnRegister);
        final EditText EditName = (EditText)findViewById(R.id.EditName);
        final EditText EditCode = (EditText)findViewById(R.id.EditCode);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                         //登陆
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String NameText = EditName.getText().toString();
                String CodeText = EditCode.getText().toString();
                Cursor cursor;
                cursor = db.query("Users",new String[]{"id","Code"},null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("id"));
                        String code = cursor.getString(cursor.getColumnIndex("Code"));
                        if (name.equals(NameText)&&code.equals(CodeText)){
                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("theName",name);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this,ZhiHuActivity.class);
                            intent.putExtra("ID",name);
                            startActivity(intent);
                            finish();
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {            //转至注册界面
                Intent intent = new Intent(LoginActivity.this,LogActivity.class);
                startActivity(intent);
            }
        });
    }
}
