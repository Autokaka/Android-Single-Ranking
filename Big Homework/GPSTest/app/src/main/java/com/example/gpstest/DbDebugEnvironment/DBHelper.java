package com.example.gpstest.DbDebugEnvironment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=4;
    private static final String DATABASE_NAME="todo.db";
    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="create table "+Note.TABLE+"("
                +Note.KEY_title+" text, "
                +Note.KEY_location+" text, "
                +Note.KEY_context+" text, "
                +Note.KEY_latitude + " double, "
                +Note.KEY_longitude + " double)";
        sqLiteDatabase.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+Note.TABLE);
        onCreate(sqLiteDatabase);
    }
}
