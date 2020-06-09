package com.test.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.game2048.dao.MyHelper;
import com.test.game2048.dao.User;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends Activity {


    private List<User> userList;
    private MyHelper myHelper;
    private MyAdapter myAdapter;
    private ListView lv;

    private ImageView backBtn;
    private ImageView clearBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        userList = new ArrayList<User>();
        myHelper = new MyHelper(this);
        lv = findViewById(R.id.listView);
        backBtn = findViewById(R.id.backBtn2);
        clearBtn = findViewById(R.id.clearBtn);
        userList=queryAll();

        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivity.super.onBackPressed();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                builder.setTitle("真的不保留这些记录吗？");
                builder.setPositiveButton("确定",listener);
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });

    }

    android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SQLiteDatabase sqLiteDatabase = myHelper.getWritableDatabase();
            int count = sqLiteDatabase.delete("user",null,null);
            myAdapter.notifyDataSetChanged();
            sqLiteDatabase.close();
        }
    };


    private List<User> queryAll(){
        SQLiteDatabase sqLiteDatabase = myHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,null,null,null,null,"score Desc");
        List<User> users = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("username"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));
            String time = cursor.getString(cursor.getColumnIndex("time"));

            users.add(new User(id,name,score,time));
        }
        cursor.close();
        sqLiteDatabase.close();
        return users;
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(SecondActivity.this,R.layout.item,null);

            TextView nameTv = view.findViewById(R.id.usernameTv);
            TextView scoreTv = view.findViewById(R.id.scoreTv);
            TextView timeTv = view.findViewById(R.id.timeTv);

            User user = userList.get(position);
            switch (position){
                case 0:
                    scoreTv.setTextColor(0xfff67c5f);
                    scoreTv.setTextSize(22);
                    break;
                case 1:
                    scoreTv.setTextColor(0xfff2b179);
                    scoreTv.setTextSize(20);
                    break;
                case 2:
                    scoreTv.setTextColor(0xffedcf72);
                    break;
            }
            nameTv.setText(user.getUsername());
            scoreTv.setText(user.getScore()+"分");
            timeTv.setText(user.getTime());

            return view;
        }
    }
}
