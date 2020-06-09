package com.test.game2048.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context){
        super(context,"game.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate方法被调用，数据库被第一次创建了!");
        //执行sql语句
        db.execSQL("create table user(_id integer primary key autoincrement,username varchar(20),score integer,time varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
