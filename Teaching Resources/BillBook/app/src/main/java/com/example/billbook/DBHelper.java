package com.example.billbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by htx on 2016/2/20.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "splite.db";
    private static final String TABLE_NAME = "count";
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + "if not exists " + TABLE_NAME + " ("
                + "id integer primary key,"
                + "count varchar,"
                + "type varchar,"
                + "date varchar,"
                + "describe varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + TABLE_NAME);
        this.onCreate(db);
    }
}
