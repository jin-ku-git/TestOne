package com.qw.adse.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "draw_db";
    public static final String DRAW_TABLE_NAME = "DrawDB";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String draw_sql = "create table if not exists " + DRAW_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT,name text,listName text)";
        sqLiteDatabase.execSQL(draw_sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String goods_sql = "DROP TABLE IF EXISTS " + DRAW_TABLE_NAME;
        sqLiteDatabase.execSQL(goods_sql);
        onCreate(sqLiteDatabase);


    }

}
