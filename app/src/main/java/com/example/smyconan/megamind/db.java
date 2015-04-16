package com.example.smyconan.megamind;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mirror on 2015/4/14.
 */
public class db extends SQLiteOpenHelper{

    public db(Context context) {
        super(context, "db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE md52name(" +
                   "fname TEXT DEFAULT \"\"," +
                   "fmd5 TEXT DEFAULT \"\")");
        db.execSQL("CREATE TABLE detail(" +
                   "fname TEXT DEFAULT \"\"," +
                   "fdate TEXT DEFAULT \"\")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersin, int newVersion) {

    }
}
