package com.example.lenovo.myknow.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.myknow.MyDatabaseHelper;
import com.example.lenovo.myknow.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class informationActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
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
        Intent intent = getIntent();
        username = intent.getStringExtra("NAME");
        Button Revise1 = (Button)findViewById(R.id.Revise1);
        Button Revise2 = (Button)findViewById(R.id.Revise2);
        final EditText nickname = (EditText)findViewById(R.id.nickname);
        final EditText phonenum = (EditText)findViewById(R.id.phonenum);
        final EditText oldCode = (EditText)findViewById(R.id.oldCode);
        final EditText newCode = (EditText)findViewById(R.id.newCode);
        final EditText reCode = (EditText)findViewById(R.id.reCode);
        Revise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nametext = nickname.getText().toString();
                String Numtext = phonenum.getText().toString();
                boolean name = checkUsername(Nametext);
                boolean num = checkPhoneNumber(Numtext);
                if(name&&num){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("NickName",Nametext);
                    db.update("Users", values,"id=?", new String[] {username});
                    values.clear();
                    values.put("Phone",Numtext);
                    db.update("Users", values,"id=?", new String[] {username});
                    values.clear();
                    Toast.makeText(informationActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(informationActivity.this,"错误",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Revise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldtext = oldCode.getText().toString();
                String newtext = newCode.getText().toString();
                String retext = reCode.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor;
                cursor = db.query("Users",new String[]{"id","Code"},null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("id"));
                        String code = cursor.getString(cursor.getColumnIndex("Code"));
                        if (name.equals(username)){
                            if(code.equals(oldtext)){
                                if(checkCode(retext)) {
                                    if (newtext.equals(retext)) {
                                        ContentValues values = new ContentValues();
                                        values.put("Code", retext);
                                        db.update("Users", values, "id=?", new String[]{username});
                                        values.clear();
                                        Toast.makeText(informationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(informationActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(informationActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(informationActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
    public static boolean checkUsername(String username) {
        String regex = "([\\u4e00-\\u9fa50-9a-zA-Z]{1,12})";
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
}
