package com.example.smyconan.megamind;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by mirror on 2015/4/14.
 */
public class db extends SQLiteOpenHelper{

    public db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "db", factory, version);
    }

    public void onCreate(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersin, int newVersion) {

    }
}
