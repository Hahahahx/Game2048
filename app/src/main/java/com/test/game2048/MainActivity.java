package com.test.game2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {


        final EditText et = new EditText(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("名号：");
        builder.setView(et);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String input = et.getText().toString();
                if (input.equals("")) {
                    Toast.makeText(getApplicationContext(), "请务必留下您的大名！" + input, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name", input);
                    intent.setClass(MainActivity.this, IndexActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }



    public void rank(View view){
        Intent intent = new Intent();
        intent.setClass(this, SecondActivity.class);
        startActivity(intent);
    }



    public void onBack(View view) {
        super.onBackPressed();
    }
}
