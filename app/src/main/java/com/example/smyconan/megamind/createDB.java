package com.example.smyconan.megamind;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Smyconan on 15/4/16.
 */
public class createDB {
    public static SQLiteDatabase dbwrite;
    public static void Create(db dbtest){
        dbwrite = dbtest.getWritableDatabase();
        ContentValues cv;

        cv = new ContentValues();
        cv.put("fname","手机管家广告");
        cv.put("fmd5", "030ba16df0a060e3815392a4e4e22d63");
        dbwrite.insert("md52name", null, cv);
        cv = new ContentValues();
        cv.put("fname","手机管家广告");
        cv.put("fdate","2015-4-16");
        dbwrite.insert("detail",null,cv);

        cv = new ContentValues();
        cv.put("fname","腾讯读书广告");
        cv.put("fmd5", "a9a58b59625b7d413c2a0348efd41ab4");
        dbwrite.insert("md52name",null,cv);
        cv = new ContentValues();
        cv.put("fname","腾讯读书广告");
        cv.put("fdate","2015-4-16");
        dbwrite.insert("detail",null,cv);
    }
}
