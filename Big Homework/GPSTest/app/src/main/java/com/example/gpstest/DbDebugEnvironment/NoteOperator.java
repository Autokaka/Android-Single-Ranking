package com.example.gpstest.DbDebugEnvironment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteOperator {
    private DBHelper dbHelper;
    public NoteOperator(Context context){
        dbHelper=new DBHelper(context);
    }
        /**     * 插入数据
         * * @param note
         * * @return     */
        public boolean insert(Note note){
            //与数据库建立连接
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Note.KEY_title,note.title);
            contentValues.put(Note.KEY_location,note.location);
            contentValues.put(Note.KEY_context,note.context);
            contentValues.put(Note.KEY_latitude,note.latitude);//插入每一行数据
            contentValues.put(Note.KEY_longitude,note.longitude);
            long note_id=db.insert(Note.TABLE,null,contentValues);
            db.close();
            if(note_id!=-1)
                return true;
            else
                return false;    }
    public void delete(int n){        //与数据库建立连接
        //与数据库建立连接
        HashMap<String,String> hash;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="select "+Note.KEY_title+","+Note.KEY_location+","+Note.KEY_context+
                " from "+Note.TABLE;
        ArrayList<HashMap<String,String>> noteList=new ArrayList<HashMap<String,String>>();
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            HashMap<String,String> note=new HashMap<String,String>();
            note.put("title",cursor.getString(cursor.getColumnIndex(Note.KEY_title)));
            note.put("location",cursor.getString(cursor.getColumnIndex(Note.KEY_location)));
            note.put("context",cursor.getString(cursor.getColumnIndex(Note.KEY_context)));
            noteList.add(note);
        }
        cursor.close();
        hash=noteList.get(n);
        String str=hash.get("title");
        db.delete(Note.TABLE,Note.KEY_title+"=?",new String[]{str});
        db.close();
        }

    public ArrayList<HashMap<String,String>> getNoteList(){
            //与数据库建立连接
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="select "+Note.KEY_title+","+Note.KEY_location+","+Note.KEY_context+
                " from "+Note.TABLE;
        ArrayList<HashMap<String,String>> noteList=new ArrayList<HashMap<String,String>>();
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            HashMap<String,String> note=new HashMap<String,String>();
            note.put("title",cursor.getString(cursor.getColumnIndex(Note.KEY_title)));
            note.put("location",cursor.getString(cursor.getColumnIndex(Note.KEY_location)));
            noteList.add(note);
        }
        cursor.close();
        db.close();
        return noteList;
        }

    public HashMap<String,String> getonehashmap(int n){
        //与数据库建立连接
        HashMap<String,String> hash;
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql="select "+Note.KEY_title+","+Note.KEY_location+","+Note.KEY_context+","+Note.KEY_latitude+","+Note.KEY_longitude+
                " from "+Note.TABLE;
        ArrayList<HashMap<String,String>> noteList=new ArrayList<HashMap<String,String>>();
        Cursor cursor=db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            HashMap<String,String> note=new HashMap<String,String>();
            note.put("title",cursor.getString(cursor.getColumnIndex(Note.KEY_title)));
            note.put("location",cursor.getString(cursor.getColumnIndex(Note.KEY_location)));
            note.put("context",cursor.getString(cursor.getColumnIndex(Note.KEY_context)));
            note.put("latitude",String.valueOf(cursor.getDouble(cursor.getColumnIndex(Note.KEY_latitude))));
            note.put("longitude",String.valueOf(cursor.getDouble(cursor.getColumnIndex(Note.KEY_longitude))));
            noteList.add(note);
        }
        cursor.close();
        db.close();
        hash=noteList.get(n);
        return hash;
    }

    public ArrayList<LatLng> getLocationList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from " + Note.TABLE;
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        Cursor cursor = db.rawQuery(sql, null);
        double longitude, latitude;
        while (cursor.moveToNext()) {
            longitude = cursor.getDouble(cursor.getColumnIndex(Note.KEY_longitude));
            latitude = cursor.getDouble(cursor.getColumnIndex(Note.KEY_latitude));
            LatLng oneline = new LatLng(latitude, longitude);
            list.add(oneline);
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<String> getAffair(ArrayList<LatLng> locList) {
        ArrayList<String> affairList = new ArrayList<String>();
        if (locList != null && !locList.isEmpty()) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String oneline, sql;
            Cursor cursor = null;
            for (int i = 0; i < locList.size(); i++) {
                sql = "select title, context from " + Note.TABLE +
                             " where " + Note.KEY_longitude + "=" + locList.get(i).longitude + " and " + Note.KEY_latitude + "=" + locList.get(i).latitude;
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    oneline = cursor.getString(cursor.getColumnIndex(Note.KEY_title)) + ": " + cursor.getString(cursor.getColumnIndex(Note.KEY_context));
                    affairList.add(oneline);
                }
            }
            cursor.close();
            db.close();
        }
        return affairList;
    }

    public void deleteAffair(ArrayList<LatLng> matchList) {
        if (matchList != null && !matchList.isEmpty()) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = null;
            for (int i = 0; i < matchList.size(); i++) {
                sql = "delete from " + Note.TABLE +
                      " where " + Note.KEY_longitude + "=" + matchList.get(i).longitude + " and " + Note.KEY_latitude + "=" + matchList.get(i).latitude;
                db.execSQL(sql);
            }
            db.close();
        }
    }


}
