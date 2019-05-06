package com.example.billbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by htx on 2016/2/20.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "count";

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void insert(Count count) {
        db.beginTransaction();  //开始事务
        try {
            ContentValues cv = new ContentValues();
            cv.put("count",count.getMoney());
            cv.put("type",count.getType());
            cv.put("date", count.getDate());
            cv.put("describe",count.getDescribe());
            db.insert(TABLE_NAME,"id",cv);
            db.setTransactionSuccessful();  //设置事务成功完成
        }finally {
            db.endTransaction();    //结束事务
        }
    }

    public Double getResult(int type)
    {
        Double result = 0.0;
        Cursor c = db.rawQuery("select id,count,type,date,describe from "+ TABLE_NAME,null);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
            if (c.getInt(2) == type)
                result += Double.parseDouble(c.getString(1));
        }
        c.close();
        return result;
    }
    public void closeDB(){
        db.close();
    }
}
