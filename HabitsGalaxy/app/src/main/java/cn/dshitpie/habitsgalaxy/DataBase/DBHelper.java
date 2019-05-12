package cn.dshitpie.habitsgalaxy.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //数据库配置
    private static final String TAG = "HabitsGalaxy";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "HabitsGalaxy.db";

    //初始化函数
    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建Globe表
        String sql = "create table "+ Planet.TABLE_NAME
                + "("

                + Planet.KEY_PLANET_ID + " text PRIMARY KEY, "
                + Planet.KEY_TITLE + " text, "
                + Planet.KEY_TREE_NUM + " text, "
                + Planet.KEY_START_DATE + " text, "
                + Planet.KEY_END_DATE + " text"

                + ") ";
        db.execSQL(sql);

    }

    //针对生成表时, 表已经存在的情况
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + Planet.TABLE_NAME);
        onCreate(db);
    }

    //插入星球数据
    public boolean insertPlanet(Planet planet) {

        Log.d(TAG, "DBHelper.insertPlanet");
        //与数据库建立连接
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Planet.KEY_PLANET_ID, planet.PLANET_ID);
        contentValues.put(Planet.KEY_TITLE, planet.TITLE);
        contentValues.put(Planet.KEY_TREE_NUM, "0");
        contentValues.put(Planet.KEY_START_DATE, planet.START_DATE);
        contentValues.put(Planet.KEY_END_DATE, planet.END_DATE);
        long response = db.insert(Planet.TABLE_NAME, null, contentValues);
        db.close();
        if(response == -1) return false;
        return false;

    }

    //获取数据库内已存数据行数
    public int countPlanet() {

        Log.d(TAG, "DBHelper.countRow");
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select count(*) from " + Planet.TABLE_NAME;
        SQLiteStatement request = db.compileStatement(sql);
        int response = (int) request.simpleQueryForLong();
        db.close();
        return response;

    }

    public boolean updatePlanetTree(Planet planet) {

        Log.d(TAG, "DBHelper.updatePlanet");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Planet.KEY_TREE_NUM, planet.TREE_NUM);
        long response = db.update(Planet.TABLE_NAME, contentValues, Planet.KEY_PLANET_ID + "=?", new String[] {planet.PLANET_ID});
        db.close();
        if (response == -1) return false;
        return true;
    }

    //获取全部星球数据
    public ArrayList<Planet> getAllPlanets() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Planet> list = new ArrayList<Planet>();
        String sql = "select * from " + Planet.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        Planet response = new Planet();
        while (cursor.moveToNext()) {
            response.PLANET_ID = cursor.getString(cursor.getColumnIndex(Planet.KEY_PLANET_ID));
            response.TITLE = cursor.getString(cursor.getColumnIndex(Planet.KEY_TITLE));
            response.TREE_NUM = cursor.getString(cursor.getColumnIndex(Planet.KEY_TREE_NUM));
            response.START_DATE = cursor.getString(cursor.getColumnIndex(Planet.KEY_START_DATE));
            response.END_DATE = cursor.getString(cursor.getColumnIndex(Planet.KEY_END_DATE));
            list.add(response);
        }
        cursor.close();
        db.close();
        return list;

    }

}
