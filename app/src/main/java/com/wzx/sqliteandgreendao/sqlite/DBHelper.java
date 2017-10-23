package com.wzx.sqliteandgreendao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述 TODO
 * Created by 王治湘 on 2017/10/23.
 * version 1.0
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "note_sqlite.db";
    public static final String TABLE_NAME = "table_notes";
    public static final String ID = "id";
    public static final String TEXT = "text";
    public static final String COMMENT = "comment";
    public static final String DATE = "date";
    public static final String TYPE = "type";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TABLE_NAME
                + " ("
                + ID + " integer PRIMARY KEY AUTOINCREMENT, "
                + TEXT + " varchar(20), "
                + COMMENT + " varchar(100), "
                + DATE + " varchar(16), "
                + TYPE + " varchar(10))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
