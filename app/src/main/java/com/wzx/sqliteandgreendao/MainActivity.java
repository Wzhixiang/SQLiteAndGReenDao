package com.wzx.sqliteandgreendao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 描述 TODO
 * Created by 王治湘 on 2017/10/23.
 * version 1.0
 */

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onToSQLite(View view){
        startActivity(new Intent(this, SQLiteActivity.class));
    }

    public void onToGreenDao(View view){
        startActivity(new Intent(this, GreenDaoActivity.class));
    }
}
