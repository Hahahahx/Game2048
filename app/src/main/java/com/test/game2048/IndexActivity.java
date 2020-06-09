package com.test.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.game2048.dao.MyHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IndexActivity extends Activity {

    int[][] numDouble;
    int[] num;
    int score;
    TextView scoreTv;
    ImageView backBtn;
    ImageView reGameBtn;
    TextView userTv;
    String name;
    MyHelper myHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        initialize();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userTv.setText(name);

        ViewGroupOnTouchEvent((ViewGroup) findViewById(R.id.relativeLayout));

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                builder.setTitle("真的不继续了吗？");
                builder.setPositiveButton("确定",listener);
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        reGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialize();
            }
        });
    }

    private void insert(){
        SQLiteDatabase sqLiteDatabase = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username",name);
        values.put("score",score);
        SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
        values.put("time",data.format(new Date()));
        sqLiteDatabase.insert("user",null,values);
        sqLiteDatabase.close();
    }

    android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            insert();
            IndexActivity.super.onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
        builder.setTitle("真的不继续了吗？");
        builder.setPositiveButton("确定",listener);
        builder.setNegativeButton("取消",null);
        builder.show();

    }

    private void ViewGroupOnTouchEvent(ViewGroup viewGroup){
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            float x1 =0;
            float y1 =0;
            float x2 =0;
            float y2 =0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //继承了Activity的onTouchEvent方法，直接监听点击事件
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if(y1 - y2 > 50 &&  x1-x2<y1-y2 && x2-x1<y1-y2) {
                        swipeUp();
                        flash();
                    } else if(y2 - y1 > 50 &&  x1-x2<y2-y1 && x2-x1<y2-y1) {
                        swipeDown();
                        flash();
                    } else if(x1 - x2 > 50 ) {
                        swipeLeft();
                        flash();
                    } else if(x2 - x1 > 50) {
                        swipeRight();
                        flash();
                    }
                }
                return true;
            }
        });
    }

    private void flash(){
        scoreTv.setText(score+"");
        if (randomNumberAndContinue()){
            transNumber();
            setTvs((ViewGroup) findViewById(R.id.relativeLayout));
        }else{
            Toast.makeText(IndexActivity.this,"游戏结束，得分"+score,Toast.LENGTH_SHORT).show();
        }
    }







    private void initialize(){
        myHelper = new MyHelper(this);
        scoreTv = findViewById(R.id.scoreTv);
        userTv = findViewById(R.id.userTv);
        backBtn = findViewById(R.id.backBtn);
        reGameBtn = findViewById(R.id.reGameBtn);
        numDouble = new int[4][4];
        num = new int[16];
        score = 0;
        randomNumberAndContinue();
        transNumber();
        setTvs((ViewGroup) findViewById(R.id.relativeLayout));
    }


    public void transNumber(){
        int index = 0;
        for(int i=0;i<numDouble.length;i++){//控制行数
            for(int j=0;j<numDouble[i].length;j++){//一行中有多少个元素（即多少列）
                num[index++]=numDouble[j][i];           //有一点点问题——————————————？
            }
        }
    }


    private boolean randomNumberAndContinue(){
        for(int n :num){
            if(n==0){
                while(true){
                    Random random = new Random();
                    int i = random.nextInt(4);
                    int j = random.nextInt(4);
                    if(numDouble[i][j] == 0){
                        numDouble[i][j] = random.nextInt(4)+1 == 4 ? 4 : 2;
                        return true;
                    }
                }
            }
        }

        for(int i =0 ;i<numDouble.length;i++){
            for (int j =0 ;j<numDouble[i].length;j++){
                if(i!=numDouble.length-1){
                    if(numDouble[i][j] == numDouble[i+1][j]){
                        return true;
                    }
                }
                if(j!=numDouble[i].length-1){
                    if(numDouble[i][j] == numDouble[i][j+1]){
                        return true;
                    }
                }
            }
        }
        return false;

    }

    private void setTvs(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            int j = 0;
            if (view instanceof TextView) {
                TextView numTv = (TextView)view;
                if (num[i] <= 0){
                    numTv.setText("");
                }else {
                    numTv.setText(num[i]+"");
                }
                switch (num[i]) {
                    case 0:
                        numTv.setBackgroundColor(0x33ffffff);
                        break;
                    case 2:
                        numTv.setBackgroundColor(0xffeee4da);
                        break;
                    case 4:
                        numTv.setBackgroundColor(0xffede0c8);
                        break;
                    case 8:
                        numTv.setBackgroundColor(0xfff2b179);
                        break;
                    case 16:
                        numTv.setBackgroundColor(0xfff59563);
                        break;
                    case 32:
                        numTv.setBackgroundColor(0xfff67c5f);
                        break;
                    case 64:
                        numTv.setBackgroundColor(0xfff65e3b);
                        break;
                    case 128:
                        numTv.setBackgroundColor(0xffedcf72);
                        break;
                    case 256:
                        numTv.setBackgroundColor(0xffedcc61);
                        break;
                    case 512:
                        numTv.setBackgroundColor(0xffedc850);
                        break;
                    case 1024:
                        numTv.setBackgroundColor(0xffedc53f);
                        break;
                    case 2048:
                        numTv.setBackgroundColor(0xffedc22e);
                        break;
                }
            }
        }
    }


    private void swipeLeft() { // 往左滑动手指
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (numDouble[x1][y] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x1][y];
                            numDouble[x1][y]=0;
                            x--;
                            break;
                        } else if(numDouble[x][y]==numDouble[x1][y]){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x1][y]=0;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeRight() {
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >=0; x--) {
                for (int x1 = x - 1; x1 >=0; x1--) {
                    if (numDouble[x1][y] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x1][y];
                            numDouble[x1][y]=0;

                            x++;
                            break;
                        } else if(numDouble[x][y]==numDouble[x1][y]){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x1][y]=(0);
                            break;
                        }
                    }
                }
            }
        }


    }

    private void swipeUp() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (numDouble[x][y1] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x][y1];
                            numDouble[x][y1]=0;
                            y--;
                            break;
                        } else if(numDouble[x][y]==numDouble[x][y1]){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=(numDouble[x][y]*2);
                            numDouble[x][y1]=(0);
                            break;
                        }
                    }
                }
            }
        }

    }

    private void swipeDown() {

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >=0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (numDouble[x][y1] > 0) {

                        if (numDouble[x][y] <= 0) {
                            numDouble[x][y]=numDouble[x][y1];
                            numDouble[x][y1]=0;
                            y++;
                            break;
                        } else if(numDouble[x][y]==numDouble[x][y1]){
                            score = score+numDouble[x][y];  //加分咯
                            numDouble[x][y]=numDouble[x][y]*2;
                            numDouble[x][y1]=0;
                            break;
                        }
                    }
                }
            }
        }
    }
}
