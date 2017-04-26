package com.yandex.barracuda.ttranslator.screens.extra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Barracuda on 21.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table ttable ("
                + "id integer primary key autoincrement,"
                + "fav_state integer,"
                + "word text,"
                + "translate text,"
                + "from_to text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
