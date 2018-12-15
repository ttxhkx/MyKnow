package com.example.lenovo.myknow.Activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarLog);
        setSupportActionBar(toolbar);
        toolbar.setTitle("知乎日报");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbHelper = new MyDatabaseHelper(this,"Users.db",null,1);
        Button btn1 = (Button)findViewById(R.id.btn1);
        Button btn2 = (Button)findViewById(R.id.btn2);
        final EditText thename = (EditText)findViewById(R.id.thename);
        final EditText thecode = (EditText)findViewById(R.id.thecode);
        final EditText recode = (EditText)findViewById(R.id.therecode);
        final EditText phone = (EditText)findViewById(R.id.phone);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int op = 0;
                String Nametext = thename.getText().toString();
                String Codetext = thecode.getText().toString();
                String ReCodetext = recode.getText().toString();
                String Phonetext = phone.getText().toString();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user);
                byte[] user = getBitmapByte(bitmap);
                Log.e("TAG", String.valueOf(user));
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                boolean code = checkCode(Codetext);
                if (code) {
                    if (ReCodetext.equals(Codetext)) {
                        Cursor cursor;
                        cursor = db.query("Users", new String[]{"id"}, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            do {
                                String name = cursor.getString(cursor.getColumnIndex("id"));
                                if (name.equals(Nametext)) {
                                    Toast.makeText(LogActivity.this, "该用户名已注册", Toast.LENGTH_SHORT).show();
                                    op = op + 1;
                                }
                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                        boolean name = checkUsername(Nametext);
                        boolean num = checkPhoneNumber(Phonetext);
                        if (name && num && op == 0) {
                            ContentValues values = new ContentValues();
                            values.put("id", Nametext);
                            values.put("NickName", Nametext);
                            values.put("Code", Codetext);
                            values.put("Phone", Phonetext);
                            values.put("ImageId", user);
                            db.insert("Users", null, values);
                            values.clear();
                            Toast.makeText(LogActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            if (op == 0) {
                                Toast.makeText(LogActivity.this, "请填入正确信息", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(LogActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(LogActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public static boolean checkUsername(String username) {
        String regex = "([a-zA-Z0-9]{4,12})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }
    public static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern=Pattern.compile("^1[0-9]{10}$");
        Matcher matcher=pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public static boolean checkCode(String code){
        Pattern pattern=Pattern.compile("[\\S]{8,16}$");
        Matcher matcher=pattern.matcher(code);
        return matcher.matches();
    }
    public byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
